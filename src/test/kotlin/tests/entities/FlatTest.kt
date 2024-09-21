  package tests.entities

import CreateWithXmlElement
import Xml
import XmlElemental
import Startup
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Flat
import entities.SetPlatform
import entities.Setpiece
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class FlatTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode("flat")
    xmlElement.setAttribute("x1", "0.1")
    xmlElement.setAttribute("y1", "0.2")
    xmlElement.setAttribute("x2", "0.3")
    xmlElement.setAttribute("y2", "0.4")
    return xmlElement
  }

  private fun minimalSetPiece(): Setpiece {
    val setPieceElement = IIOMetadataNode("flat")
    setPieceElement.setAttribute("x", "0.1")
    setPieceElement.setAttribute("y", "0.2")
    return Setpiece.factory(setPieceElement, null)
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is elemental`() {
    val xmlElement = IIOMetadataNode("flat")

    val setPiece = Flat.factory(xmlElement, null)

    assertIs<XmlElemental>(setPiece)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Flat>>(Flat)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Flat.Tag).isEqualTo("flat")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Flat.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Flat>(Flat.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Flat.factory(minimalXml(), minimalSetPiece())

    SoftAssertions().apply {
      assertThat(instance.start).isEqualTo(StagePoint(0.1F, 0.2f, 0f))
      assertThat(instance.end).isEqualTo(StagePoint(0.3F, 0.4f, 0f))
      assertThat(instance.errors).hasSize(0)
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode("flat")

    val instance = Flat.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "flat missing required x1 attribute",
        "flat missing required y1 attribute",
        "flat missing required x2 attribute",
        "flat missing required y2 attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode("flat")
    xmlElement.setAttribute("x1", "bogus.1")
    xmlElement.setAttribute("y1", "bogus.2")
    xmlElement.setAttribute("x2", "bogus.1")
    xmlElement.setAttribute("y2", "bogus.2")

    val instance = Flat.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "flat unable to read floating-point number from x1 attribute",
        "flat unable to read floating-point number from y1 attribute",
        "flat unable to read floating-point number from x2 attribute",
        "flat unable to read floating-point number from y2 attribute",
      )
    }.assertAll()
  }

//  @Test
//  fun `adopt keeps a reference to child shape`() {
//    val instance = Flat.factory(minimalXml(), null)
//
//    val setPlatformElement = IIOMetadataNode("flat)
//    setPlatformElement.setAttribute("x", "17.6")
//    setPlatformElement.setAttribute("y", "124")
////    setPlatformElement.setAttribute("rectangle", "17.6  124")
//    val setPlatform = SetPlatform.factory(setPlatformElement, instance)
//
//    assertThat(instance.parts).contains(setPlatform)
//  }

}
