package tests.entities

import CreateWithXmlElement
import Svg
import XmlElemental
import entities.Drawing
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class DrawingTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "Name of drawing")
    xmlElement.setAttribute("filename", "file-name")
    xmlElement.setAttribute("pipe", "pipe-name")
    return xmlElement
  }

//  @AfterTest
//  fun cleanup() {
//    unmockkObject(Svg)
//  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val drawing = Drawing.factory(xmlElement, null)

    assertIs<XmlElemental>(drawing)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Drawing>>(Drawing)
  }

  @Test
  fun `companion has tag`() {
    Assertions.assertThat(Drawing.Tag).isEqualTo("drawing")
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Drawing>(Drawing.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Drawing.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("Name of drawing")
      assertThat(instance.filename).isEqualTo("file-name")
      assertThat(instance.pipe).isEqualTo("pipe-name")
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Drawing.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required id attribute",
        "Missing required filename attribute",
        "Missing required pipe attribute",
      )
    }.assertAll()
  }

}