package tests.entities

import CreateWithXmlElement
import Xml
import XmlElemental
import Startup
import coordinates.StagePoint
import entities.Setpiece
import entities.SetPlatform
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class SetpieceTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("setpiece")
    xmlElement.setAttribute("x", "0.1")
    xmlElement.setAttribute("y", "0.2")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode("setpiece")

    val setPiece = Setpiece.factory(xmlElement, null)

    assertIs<XmlElemental>(setPiece)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Setpiece>>(Setpiece)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Setpiece.Tag).isEqualTo("setpiece")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Setpiece.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Setpiece>(Setpiece.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Setpiece.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(StagePoint(0.1F, 0.2f, 0f))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("setpiece")

    val instance = Setpiece.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "setpiece missing required x attribute",
        "setpiece missing required y attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("setpiece")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")

    val instance = Setpiece.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "setpiece unable to read floating-point number from x attribute",
        "setpiece unable to read floating-point number from y attribute",
      )
    }.assertAll()
  }

  @Test
  fun `adopt keeps a reference to child shape`() {
    val instance = Setpiece.factory(minimalXml(), null)

    val setPlatformElement = IIOMetadataNode("setpiece")
    setPlatformElement.setAttribute("x", "17.6")
    setPlatformElement.setAttribute("y", "124")
//    setPlatformElement.setAttribute("rectangle", "17.6  124")
    val setPlatform = SetPlatform.factory(setPlatformElement, instance)

    assertThat(instance.parts).contains(setPlatform)
  }

}
