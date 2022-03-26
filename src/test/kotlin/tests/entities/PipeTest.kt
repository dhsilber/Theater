package tests.entities

import CreateWithXmlElement
import entities.Pipe
import XmlElemental
import entities.Proscenium
import io.mockk.every
import io.mockk.mockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PipeTest {

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val luminaire = Pipe.factory(xmlElement)

    assertIs<XmlElemental>(luminaire)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Pipe>>(Pipe)
  }

  @Test
  fun `companion has tag`() {
    assertEquals("pipe", Pipe.Tag)
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "1.2")
    xmlElement.setAttribute("y", "2.3")
    xmlElement.setAttribute("z", "3.4")
    xmlElement.setAttribute("length", "4.5")

    val instance = Pipe.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("name")
      assertThat(instance.x).isEqualTo(1.2f)
      assertThat(instance.y).isEqualTo(2.3f)
      assertThat(instance.z).isEqualTo(3.4f)
      assertThat(instance.length).isEqualTo(4.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Pipe.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required id attribute",
        "Missing required x attribute",
        "Missing required y attribute",
        "Missing required z attribute",
        "Missing required length attribute",
        )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("z", "bogus.3")
    xmlElement.setAttribute("length", "bogus.4")

    val instance = Pipe.factory(xmlElement)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
        "Unable to read floating-point number from z attribute",
        "Unable to read positive floating-point number from length attribute",
      )
    }.assertAll()
  }

  @Test
  fun `reorient using proscenium coordinates`() {
    val prosceniumElement = IIOMetadataNode()
    prosceniumElement.setAttribute("x", "120")
    prosceniumElement.setAttribute("y", "230")
    prosceniumElement.setAttribute("z", "34")
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertEquals(false, Proscenium.inUse())
    Proscenium.factory(prosceniumElement)
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Pipe.factory(xmlElement)

    Pipe.reorientForProsceniumOrigin()

    SoftAssertions().apply {
      assertThat(instance.x).isEqualTo(130f)
      assertThat(instance.y).isEqualTo(210f)
      assertThat(instance.z).isEqualTo(334f)
    }.assertAll()
  }

}
