package tests.entities

import CreateWithXmlElement
import Point
import XmlElemental
import entities.Wall
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertIs

class WallTest {

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode()

    val wall = Wall.factory(xmlElement, null)

    assertIs<XmlElemental>(wall)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Wall>>(Wall)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Wall.Tag).isEqualTo("wall")
  }

  @Test
  fun `has required attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x1", "0.1")
    xmlElement.setAttribute("y1", "0.2")
    xmlElement.setAttribute("x2", "0.3")
    xmlElement.setAttribute("y2", "0.4")

    val instance = Wall.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.start).isEqualTo(Point(0.1F, 0.2f, 0f))
      assertThat(instance.end).isEqualTo(Point(0.3F, 0.4f, 0f))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Wall.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required x1 attribute",
        "Missing required y1 attribute",
        "Missing required x2 attribute",
        "Missing required y2 attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x1", "bogus.1")
    xmlElement.setAttribute("y1", "bogus.2")
    xmlElement.setAttribute("x2", "bogus.3")
    xmlElement.setAttribute("y2", "bogus.4")

    val instance = Wall.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from x1 attribute",
        "Unable to read floating-point number from y1 attribute",
        "Unable to read floating-point number from x2 attribute",
        "Unable to read floating-point number from y2 attribute",
      )
    }.assertAll()
  }
}
