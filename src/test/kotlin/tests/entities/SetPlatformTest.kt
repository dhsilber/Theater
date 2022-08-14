package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import coordinates.StagePoint
import entities.SetPlatform
import entities.Setpiece
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

class SetPlatformTest {

  val x = 2.3f
  val y = 17.6f
  val z = 9.5f

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", x.toString())
    xmlElement.setAttribute("y", y.toString())
    xmlElement.setAttribute("z", z.toString())
//    xmlElement.setAttribute("z", z.toString())
    return xmlElement
  }

  private fun minimalSetPiece(): Setpiece {
    val setPieceElement = IIOMetadataNode()
    setPieceElement.setAttribute("x", "0.1")
    setPieceElement.setAttribute("y", "0.2")
    return Setpiece.factory(setPieceElement, null)
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val setPlatform = SetPlatform.factory(xmlElement, null)

    assertIs<XmlElemental>(setPlatform)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<SetPlatform>>(SetPlatform)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<SetPlatform>(SetPlatform.factory(minimalXml(), null))
  }

  @Test
  fun `companion has tag`() {
    assertThat(SetPlatform.Tag).isEqualTo("set-platform")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(SetPlatform.Tag)
  }

  @Test
  fun `has required attributes`() {
    val instance = SetPlatform.factory(minimalXml(), minimalSetPiece())

    SoftAssertions().apply {
//      assertThat(instance.origin).isEqualTo(PointOffset(x, y, 0f))
      assertThat(instance.origin).isEqualTo(StagePoint(x, y, z))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val xmlElement = minimalXml()
    xmlElement.setAttribute("z", z.toString())

    val instance = SetPlatform.factory(xmlElement, minimalSetPiece())

    SoftAssertions().apply {
//      assertThat(instance.origin).isEqualTo(PointOffset(x, y, z))
      assertThat(instance.origin).isEqualTo(StagePoint(x, y, z))
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = SetPlatform.factory(xmlElement, minimalSetPiece())

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required x attribute",
        "Missing required y attribute",
        "Missing required z attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "unavailable setPiece")
    xmlElement.setAttribute("y", "Type value")
    xmlElement.setAttribute("z", "Type value")

    val instance = SetPlatform.factory(xmlElement, minimalSetPiece())

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
        "Unable to read floating-point number from z attribute",
      )
    }.assertAll()
  }

  @Test
  fun `registers self with parent setPiece`() {
    val setPieceElement = minimalXml()
    setPieceElement.setAttribute("id", "setPiece-name")
    val setPiece = Setpiece.factory(setPieceElement, null)

    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")

    mockkObject(setPiece)
    val setPlatformSlot = slot<SetPlatform>()
    every { setPiece.adopt(dependant = capture(setPlatformSlot)) } returns Unit

    val instance = SetPlatform.factory(xmlElement, setPiece)

    verify(exactly = 1) { setPiece.adopt(dependant = instance) }
  }

  @Test
  fun `requires parent setPiece`() {
    val setPlatformElement = minimalXml()
    val setPlatform = SetPlatform.factory(setPlatformElement, null)

    SoftAssertions().apply {
      assertThat(setPlatform.hasError).isTrue
      assertThat(setPlatform.errors).containsExactly(
        "Set-platform is not associated with a parent setpiece",
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
//    val setPlatformSlot = slot<SetPlatform>()
//    every { setPiece.hang(dependant = capture(setPlatformSlot)) } returns Unit
//
//    val instance = SetPlatform.factory(xmlElement, null)
//
//    verify(exactly = 1) { setPiece.hang(dependant = instance) }
//  }

  @Test
  fun `adopt keeps a reference to child shape`() {
    val instance = SetPlatform.factory(minimalXml(), null)

    val shapeElement = IIOMetadataNode()
    shapeElement.setAttribute("rectangle", "17.6  124")
    val shape = Shape.factory(shapeElement, instance)

    assertThat(instance.shapes).contains(shape)
  }

}
