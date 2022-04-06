package tests.entities

import CreateWithXmlElement
import XmlElemental
import entities.LuminaireDefinition
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

class LuminaireDefinitionTest {

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val element = LuminaireDefinition.factory(xmlElement, null)

    assertIs<XmlElemental>(element)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<LuminaireDefinition>>(LuminaireDefinition)
  }

  @Test
  fun `companion has tag`() {
    Assertions.assertThat(LuminaireDefinition.Tag).isEqualTo("luminaire-definition")
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "1.2")

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.name).isEqualTo("name")
      assertThat(instance.weight).isEqualTo(1.2f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "1.2")
    xmlElement.setAttribute("complete", "1")
    xmlElement.setAttribute("width", "2.3")
    xmlElement.setAttribute("length", "3.4")

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.complete).isEqualTo(true)
      assertThat(instance.width).isEqualTo(2.3f)
      assertThat(instance.length).isEqualTo(3.4f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required name attribute",
        "Missing required weight attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("name", "name")
    xmlElement.setAttribute("weight", "-1.2")
    xmlElement.setAttribute("complete", "2")
    xmlElement.setAttribute("width", "-2.3")
    xmlElement.setAttribute("length", "-3.4")

    val instance = LuminaireDefinition.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read positive floating-point number from weight attribute",
        "Unable to read boolean 1 from complete attribute",
        "Unable to read positive floating-point number from width attribute",
        "Unable to read positive floating-point number from length attribute",
      )
    }.assertAll()
  }

}
