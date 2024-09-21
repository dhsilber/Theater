package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import entities.Drawing
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class DrawingTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("drawing")
    xmlElement.setAttribute("id", "Name of drawing")
    xmlElement.setAttribute("filename", "file-name")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode("drawing")

    val drawing = Drawing.factory(xmlElement, null)

    assertIs<XmlElemental>(drawing)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Drawing>>(Drawing)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Drawing.Tag).isEqualTo("drawing")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Drawing.Tag)
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
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = IIOMetadataNode("drawing")
    xmlElement.setAttribute("pipe", "pipe-name")

    val instance = Drawing.factory(xmlElement, null)

    assertThat(instance.pipe).isEqualTo("pipe-name")
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("drawing")

    val instance = Drawing.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "drawing missing required id attribute",
        "drawing missing required filename attribute",
      )
    }.assertAll()
  }

  @Test
  fun `hasPipe is false when there is no pipe`() {
    val instance = Drawing.factory(minimalXml(), null)

    assertThat(instance.hasPipe).isFalse
  }

  @Test
  fun `hasPipe is true when there is a pipe`() {
    val xmlElement = IIOMetadataNode("drawing")
    xmlElement.setAttribute("pipe", "pipe-name")

    val instance = Drawing.factory(xmlElement, null)

    assertThat(instance.hasPipe).isTrue
  }

}
