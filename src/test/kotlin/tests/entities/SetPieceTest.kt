package tests.entities

import CreateWithXmlElement
import Point
import XmlElemental
import entities.SetPiece
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

class SetPieceTest {

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode()

    val setPiece = SetPiece.factory(xmlElement, null)

    assertIs<XmlElemental>(setPiece)
    assertThat(setPiece).isInstanceOfAny(::XmlElemental.class)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<SetPiece>>(SetPiece)
  }

  @Test
  fun `companion has tag`() {
    assertThat(SetPiece.Tag).isEqualTo("setpiece")
  }`

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")

    val instance = SetPiece.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(Point(0.1F, 0.2f, 0f))
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
}
