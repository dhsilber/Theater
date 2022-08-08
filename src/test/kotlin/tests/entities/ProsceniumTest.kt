package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import coordinates.VenuePoint
import entities.Proscenium
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProsceniumTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "1.2")
    xmlElement.setAttribute("y", "2.3")
    xmlElement.setAttribute("z", "3.4")
    xmlElement.setAttribute("height", "4.5")
    xmlElement.setAttribute("width", "5.6")
    xmlElement.setAttribute("depth", "6.7")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val luminaire = Proscenium.factory(xmlElement, null)

    assertIs<XmlElemental>(luminaire)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Proscenium>>(Proscenium)
  }

  @Test
  fun `companion has tag`() {
    assertEquals("proscenium", Proscenium.Tag)
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup().startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Proscenium.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Proscenium>(Proscenium.factory(minimalXml(), null))
  }

  @Test
  fun `companion has test for the absence of a proscenium in this venue`() {
    Proscenium.instances.clear()
//    val venueElement = IIOMetadataNode()
//    venueElement.setAttribute("building", "Building value")
//    venueElement.setAttribute("room", "Room value")
//    venueElement.setAttribute("width", "12")
//    venueElement.setAttribute("depth", "23")
//    venueElement.setAttribute("height", "34")
//    Venue.factory(venueElement, null)

    assertEquals(false, Proscenium.inUse())
  }

  @Test
  fun `companion has test for the presence of a proscenium in this venue`() {
//    val venueElement = IIOMetadataNode()
//    venueElement.setAttribute("building", "Building value")
//    venueElement.setAttribute("room", "Room value")
//    venueElement.setAttribute("width", "12")
//    venueElement.setAttribute("depth", "23")
//    venueElement.setAttribute("height", "34")
//    Venue.factory(venueElement, null)
    val prosceniumElement = IIOMetadataNode()
    prosceniumElement.setAttribute("x", "1.2")
    prosceniumElement.setAttribute("y", "2.3")
    prosceniumElement.setAttribute("z", "3.4")
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertEquals(false, Proscenium.inUse())

    Proscenium.factory(prosceniumElement, null)

    assertEquals(true, Proscenium.inUse())
  }

  @Test
  fun `has required attributes`() {
    val instance = Proscenium.factory(minimalXml(), null)

    assertThat(instance.origin).isEqualTo(VenuePoint(1.2f, 2.3f, 3.4f))
    assertEquals(4.5f, instance.height)
    assertEquals(5.6f, instance.width)
    assertEquals(6.7f, instance.depth)
    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Proscenium.factory(xmlElement, null)

    assertTrue(instance.hasError)
    assertEquals(6, instance.errors.size)
    assertEquals("Missing required x attribute", instance.errors[0])
    assertEquals("Missing required y attribute", instance.errors[1])
    assertEquals("Missing required z attribute", instance.errors[2])
    assertEquals("Missing required height attribute", instance.errors[3])
    assertEquals("Missing required width attribute", instance.errors[4])
    assertEquals("Missing required depth attribute", instance.errors[5])
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("x", "bogus 1.2")
    xmlElement.setAttribute("y", "bogus 2.3")
    xmlElement.setAttribute("z", "bogus 3.4")
    xmlElement.setAttribute("height", "bogus 4.5")
    xmlElement.setAttribute("width", "-5.6")
    xmlElement.setAttribute("depth", "bogus 6.7")

    val instance = Proscenium.factory(xmlElement, null)

    assertTrue(instance.hasError)
    assertEquals("Unable to read floating-point number from x attribute", instance.errors[0])
    assertEquals("Unable to read floating-point number from y attribute", instance.errors[1])
    assertEquals("Unable to read floating-point number from z attribute", instance.errors[2])
    assertEquals("Unable to read positive floating-point number from height attribute", instance.errors[3])
    assertEquals("Unable to read positive floating-point number from width attribute", instance.errors[4])
    assertEquals("Unable to read positive floating-point number from depth attribute", instance.errors[5])
    assertEquals(6, instance.errors.size)
  }

//  @Test
//  fun `for svg drawing, no proscenium is drawn when none are defined`() {
//    Proscenium.instances.clear()
//
//    every { Proscenium.drawSvg(any(),any(),any())} returns Unit
//
//    val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
//    val namespace = "http://www.w3.org/2000/svg"
//    val document = domImpl.createDocument(namespace, "svg", null)
//    val svgGenerator = SVGGraphics2D(document)
//    val root = svgGenerator.root
//    root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")
//
//    drawSvgContent(document, namespace, root)
//
//
//    verify(exactly = 0) { Proscenium.drawSvg(pathName = "foo") }
//  }

//  @Test
//  fun `for svg drawing, proscenium is drawn`() {
//    val prosceniumElement = IIOMetadataNode()
//    prosceniumElement.setAttribute("x", "1.2")
//    prosceniumElement.setAttribute("y", "2.3")
//    prosceniumElement.setAttribute("z", "3.4")
//    prosceniumElement.setAttribute("height", "4.5")
//    prosceniumElement.setAttribute("width", "5.6")
//    prosceniumElement.setAttribute("depth", "6.7")
//
//    val instance = Proscenium.factory(prosceniumElement, null)
//    val spy = spyk(instance)
//    Proscenium.instances.clear()
//    Proscenium.instances.add(spy)
//
//    every { spy.drawSvg(any(), any(), any()) } returns Unit
//
//    val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
//    val namespace = "http://www.w3.org/2000/svg"
//    val document = domImpl.createDocument(namespace, "svg", null)
//    val svgGenerator = SVGGraphics2D(document)
//    val root = svgGenerator.root
//    root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")
//
//    println("Root before $root")
//    drawSvgContent(document, namespace, root)
//    println("Root after $root")
//
//    verify(exactly = 0) { spy.drawSvg(svgDocument = any(), svgNamespace = any(), parentElement = any()) }
//  }
//
//  @Test
//  fun `for svg drawing, proscenium is drawn 2`() {
//    Proscenium.instances.clear()
//    val prosceniumElement = IIOMetadataNode()
//    prosceniumElement.setAttribute("x", "1.2")
//    prosceniumElement.setAttribute("y", "2.3")
//    prosceniumElement.setAttribute("z", "3.4")
//    prosceniumElement.setAttribute("height", "4.5")
//    prosceniumElement.setAttribute("width", "5.6")
//    prosceniumElement.setAttribute("depth", "6.7")
//
//    val instance = Proscenium.factory(prosceniumElement, null)
//    mockkObject(instance)
////    val spy = spyk(instance)
////    Proscenium.instances.clear()
////    Proscenium.instances.add(spy)
//
//    every { instance.drawSvg(any(), any(), any()) } returns Unit
//
//    val domImpl: DOMImplementation = GenericDOMImplementation.getDOMImplementation()
//    val namespace = "http://www.w3.org/2000/svg"
//    val document = domImpl.createDocument(namespace, "svg", null)
//    val svgGenerator = SVGGraphics2D(document)
//    val root = svgGenerator.root
//    root.setAttribute("xmlns:plot", "http://www.davidsilber.name/namespaces/plot")
//
//    println("Root before $root")
//    drawSvgContent(document, namespace, root)
//    println("Root after $root")
//
//    verify(exactly = 0) { instance.drawSvg(svgDocument = any(), svgNamespace = any(), parentElement = any()) }
//  }

}
