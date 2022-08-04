package tests.entities

import CreateWithXmlElement
import Rectangle
import TagRegistry
import Xml
import XmlElemental
import com.mobiletheatertech.plot.Startup
import entities.SetPlatform
import entities.Shape
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class ShapeTest {
  private val width = 2.3f
  private val depth = 17.6f

  private fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("rectangle", "$width $depth")
    return xmlElement
  }

  private fun minimaSetPlatform(): SetPlatform {
    val setPlatformElement = IIOMetadataNode()
    setPlatformElement.setAttribute("x", "12.3")
    setPlatformElement.setAttribute("y", "15.0")
    return SetPlatform.factory(setPlatformElement, null)
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val shape = Shape.factory(xmlElement, null)

    assertIs<XmlElemental>(shape)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Shape>>(Shape)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Shape>(Shape.factory(minimalXml(), null))
  }

  @Test
  fun `companion has tag`() {
    assertThat(Shape.Tag).isEqualTo("shape")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup().startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Shape.Tag)
  }

  @Test
  fun `has required attributes`() {
    val instance = Shape.factory(minimalXml(), minimaSetPlatform())

    SoftAssertions().apply {
      assertThat(instance.rectangle).isEqualTo(Rectangle(width,depth))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

//  @Test
//  fun `registers optional attributes`() {
//    val xmlElement = minimalXml()
//    xmlElement.setAttribute("z", z.toString())
//
//    val instance = Shape.factory(xmlElement, null)
//
//    SoftAssertions().apply {
//      assertThat(instance.origin).isEqualTo(coordinates.PointOffset(width,depth,z))
//      assertThat(instance.hasError).isFalse
//    }.assertAll()
//  }

//  @Test
//  fun `notes error for missing required attributes`() {
//    val xmlElement = IIOMetadataNode()
//
//    val instance = Shape.factory(xmlElement, null)
//
//    SoftAssertions().apply {
//      assertThat(instance.hasError).isTrue
//      assertThat(instance.errors).containsExactly(
//        "Missing required width attribute",
//        "Missing required depth attribute",
//      )
//    }.assertAll()
//  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("rectangle", "text instead of numbers")

    val instance = Shape.factory(xmlElement, minimaSetPlatform())

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read rectangle specification from rectangle attribute containing 'text instead of numbers'",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for missing value in rectangle string`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("rectangle", "3.4")

    val instance = Shape.factory(xmlElement, minimaSetPlatform())

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read rectangle specification from rectangle attribute containing '3.4'",
      )
    }.assertAll()
  }

  @Test
  fun `registers self with parent setPlatform`() {
//    val setPieceElement = minimalXml()
//    setPieceElement.setAttribute("id", "setPiece-name")
//    val setPiece = SetPiece.factory(setPieceElement, null)
    val setPlatformElement = IIOMetadataNode()
    setPlatformElement.setAttribute("x", "1.0")
    setPlatformElement.setAttribute("y", "2.0")
    val setPlatform = SetPlatform.factory(setPlatformElement, null)

    val shapeElement = minimalXml()

    mockkObject(setPlatform)
    val shapeSlot = slot<Shape>()
    every { setPlatform.adopt(dependant = capture(shapeSlot)) } returns Unit

    val instance = Shape.factory(shapeElement, setPlatform)

    verify(exactly = 1) { setPlatform.adopt(dependant = instance) }
  }

  @Test
  fun `requires parent setPlatform`() {
    val shapeElement = minimalXml()
    val shape = Shape.factory(shapeElement, null)

    SoftAssertions().apply {
      assertThat(shape.hasError).isTrue
      assertThat(shape.errors).containsExactly(
        "Shape is not associated with a parent set-platform",
      )
    }.assertAll()
  }

//  @Test
//  fun `registers self with linked setPiece`() {
//    val setPieceElement = IIOMetadataNode()
//    setPieceElement.setAttribute("id", "setPiece name")
//    setPieceElement.setAttribute("x", "1.2")
//    setPieceElement.setAttribute("y", "2.3")
//    setPieceElement.setAttribute("z", "3.4")
//    setPieceElement.setAttribute("length", "4.5")
//    val setPiece = SetPiece.factory(setPieceElement, null)
//
//    val xmlElement = IIOMetadataNode()
//    xmlElement.setAttribute("on", "setPiece name")
//    xmlElement.setAttribute("type", "Type value")
//    xmlElement.setAttribute("location", "17.6")
//
//    mockkObject(setPiece)
//    val shapeSlot = slot<Shape>()
//    every { setPiece.hang(dependant = capture(shapeSlot)) } returns Unit
//
//    val instance = Shape.factory(xmlElement, null)
//
//    verify(exactly = 1) { setPiece.hang(dependant = instance) }
//  }

//  @Test
//  fun `change in address updates xmlElement and saves file`() {
//    val xmlElement = IIOMetadataNode()
//    xmlElement.setAttribute("type", "Type value")
//    xmlElement.setAttribute("location", "17.6")
//    xmlElement.setAttribute("owner", "Owner name")
//    xmlElement.setAttribute("address", "124")
//    val instance = Shape.factory(xmlElement, null)
//    mockkObject(Xml)
//    every { Xml.write() } returns Unit
//
//    instance.address = 421
//
//    assertThat(xmlElement.getAttribute("address")).isEqualTo("421")
//    verify { Xml.write() }
//  }

}
