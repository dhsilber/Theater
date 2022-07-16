package tests.entities

import CreateWithXmlElement
import XmlElemental
import com.mobiletheatertech.plot.Startup
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Locator
import entities.Luminaire
import entities.Pipe
import entities.Proscenium
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

class PipeTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "1.2")
    xmlElement.setAttribute("y", "2.3")
    xmlElement.setAttribute("z", "3.4")
    xmlElement.setAttribute("length", "4.5")
    return xmlElement
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val pipe = Pipe.factory(xmlElement, null)

    assertIs<XmlElemental>(pipe)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Pipe>>(Pipe)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Pipe.Tag).isEqualTo("pipe")
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Pipe>(Pipe.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Pipe.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("name")
      assertThat(instance.origin).isEqualTo(StagePoint(1.2f,2.3f,3.4f))
      assertThat(instance.length).isEqualTo(4.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Pipe.factory(xmlElement, null)

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

    val instance = Pipe.factory(xmlElement, null)

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

  // TODO Does this test really belong with Pipe?
  @Test
  fun `reorient using proscenium coordinates`() {
    Proscenium.instances.clear()
    val prosceniumElement = IIOMetadataNode()
    prosceniumElement.setAttribute("x", "120")
    prosceniumElement.setAttribute("y", "230")
    prosceniumElement.setAttribute("z", "34")
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertThat(Proscenium.inUse()).isFalse
    Proscenium.factory(prosceniumElement, null)
    assertThat(Proscenium.inUse()).isTrue
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Pipe.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.origin).isNotEqualTo(StagePoint(130f,210f,334f))
      assertThat(instance.origin.venue).isEqualTo(VenuePoint(130f,210f,334f))
    }.assertAll()
  }

  @Test
  fun `find pipe by id`() {
    Pipe.instances.clear()
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Pipe.factory(xmlElement, null)

    val foundPipe = Pipe.queryById("name")

    assertThat(foundPipe).isSameAs(instance)
  }

  @Test
  fun `do not find pipe by id`() {
    Pipe.instances.clear()

    val foundPipe = Pipe.queryById("name")

    assertThat(foundPipe).isNull()
  }

  @Test
  fun `find pipe by xml element`() {
    Pipe.instances.clear()
    Luminaire.instances.clear()
    val filename = "pipeAndLuminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup().startup(pathName)
    val luminaireXml = Luminaire.instances[0].xmlElement
    val pipeXml = Pipe.instances[0].xmlElement

    val foundPipe = Pipe.queryByXmlElement(luminaireXml.parentNode as Element)

    SoftAssertions().apply {
      assertThat(foundPipe).isNotNull
      assertThat(foundPipe?.xmlElement).isSameAs(pipeXml)
    }.assertAll()
  }

  @Test
  fun `do not find pipe by xml element`() {
    Pipe.instances.clear()
    Luminaire.instances.clear()
    val filename = "luminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup().startup(pathName)
    val luminaireXml = Luminaire.instances[0].xmlElement

    val foundPipe = Pipe.queryByXmlElement(luminaireXml)

    assertThat(foundPipe).isNull()
  }

  @Test
  fun `keeps track of child entities according to location on pipe`() {
    Pipe.instances.clear()
    val pipeElement = IIOMetadataNode()
    pipeElement.setAttribute("id", "name")
    pipeElement.setAttribute("x", "10")
    pipeElement.setAttribute("y", "20")
    pipeElement.setAttribute("z", "300")
    pipeElement.setAttribute("length", "4.5")
    val pipe = Pipe.factory(pipeElement, null)
    val luminaireElement = IIOMetadataNode()
    luminaireElement.setAttribute("type", "Type value")
    luminaireElement.setAttribute("location", "17.6")
    luminaireElement.setAttribute("owner", "Owner name")
    luminaireElement.setAttribute("address", "124")
    val luminaire = Luminaire.factory(luminaireElement, null)
    val luminaireElement2 = IIOMetadataNode()
    luminaireElement2.setAttribute("type", "Type value")
    luminaireElement2.setAttribute("location", "-17.6")
    luminaireElement2.setAttribute("owner", "Owner name")
    luminaireElement2.setAttribute("address", "124")
    val luminaire2 = Luminaire.factory(luminaireElement2, null)

    pipe.hang(luminaire)
    pipe.hang(luminaire2)

    Pipe.sortDependents()

    assertThat(pipe.dependents).containsExactly(
      Locator(-17.6f, luminaire2),
      Locator(17.6f, luminaire)
    )

  }
}
