package tests.entities

import CreateWithXmlElement
import VenuePoint
import XmlElemental
import entities.SetPiece
import entities.SetPlatform
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

class SetPieceTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")
    return xmlElement
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode()

    val setPiece = SetPiece.factory(xmlElement, null)

    assertIs<XmlElemental>(setPiece)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<SetPiece>>(SetPiece)
  }

  @Test
  fun `companion has tag`() {
    assertThat(SetPiece.Tag).isEqualTo("setpiece")
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<SetPiece>(SetPiece.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = SetPiece.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(VenuePoint(0.1F, 0.2f, 0f))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = SetPiece.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required x attribute",
        "Missing required y attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")

    val instance = SetPiece.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
      )
    }.assertAll()
  }

  @Test
  fun `adopt keeps a reference to child shape`() {
    val instance = SetPiece.factory(minimalXml(), null)

    val setPlatformElement = IIOMetadataNode()
    setPlatformElement.setAttribute("x", "17.6")
    setPlatformElement.setAttribute("y", "124")
//    setPlatformElement.setAttribute("rectangle", "17.6  124")
    val setPlatform = SetPlatform.factory(setPlatformElement, instance)

    assertThat(instance.parts).contains(setPlatform)
  }

}
