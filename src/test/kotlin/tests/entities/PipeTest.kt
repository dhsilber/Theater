package tests.entities

import CreateWithXmlElement
import Hangable
import TagRegistry
import Xml
import XmlElemental
import Startup
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Locator
import entities.Luminaire
import entities.Pipe
import entities.PipeBase
import entities.Proscenium
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.assertj.core.api.SoftAssertions
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class PipeTest {

  fun minimalXmlWithNoParent(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "1.2")
    xmlElement.setAttribute("y", "2.3")
    xmlElement.setAttribute("z", "3.4")
    xmlElement.setAttribute("length", "4.5")
    return xmlElement
  }

  fun minimalXmlWithPipeBaseParent(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("length", "47.5")
    return xmlElement
  }

  private fun minimalXmlWithVerticalPipeParent(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("length", "22.5")
    xmlElement.setAttribute("location", "38")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val pipe = Pipe.factory(xmlElement, null)

    assertIs<XmlElemental>(pipe)
  }

  @Test
  fun `is hangable`() {
    val xmlElement = IIOMetadataNode()

    val pipe = Pipe.factory(xmlElement, null)

    assertIs<Hangable>(pipe)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Pipe>>(Pipe)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Pipe.Tag).isEqualTo("pipe")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Pipe.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Pipe>(Pipe.factory(minimalXmlWithNoParent(), null))
  }

  @Test
  fun `has required attributes when there is no parent`() {
    val instance = Pipe.factory(minimalXmlWithNoParent(), null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("name")
      assertThat(instance.origin).isEqualTo(StagePoint(1.2f, 2.3f, 3.4f))
      assertThat(instance.length).isEqualTo(4.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `has required attributes when pipe base is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val baseOrigin = pipeBase.origin
    val expectedOrigin = StagePoint(baseOrigin.x, baseOrigin.y, baseOrigin.z + 2f)

    val instance = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(expectedOrigin)
      assertThat(instance.length).isEqualTo(47.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `has required attributes when vertical pipe is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)
    val baseOrigin = pipeBase.origin
    val expectedOrigin = StagePoint(baseOrigin.x - 11.25f, baseOrigin.y, baseOrigin.z + 40f)

    val instance = Pipe.factory(minimalXmlWithVerticalPipeParent(), verticalPipe)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(expectedOrigin)
      assertThat(instance.length).isEqualTo(22.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes when vertical pipe is parent`() {
    val xmlElement = minimalXmlWithVerticalPipeParent()
    xmlElement.setAttribute("offsety", "30")
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)

    val instance = Pipe.factory(xmlElement, verticalPipe)

    SoftAssertions().apply {
      assertThat(instance.offsety).isEqualTo(30f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes when there is no parent`() {
    val xmlElement = IIOMetadataNode()

    val instance = Pipe.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "Missing required id attribute",
        "Missing required x attribute",
        "Missing required y attribute",
        "Missing required z attribute",
        "Missing required length attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes when pipe base is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val xmlElement = IIOMetadataNode()

    val instance = Pipe.factory(xmlElement, pipeBase)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required length attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes when vertical pipe is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)
    val xmlElement = IIOMetadataNode()

    val instance = Pipe.factory(xmlElement, verticalPipe)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required length attribute",
        "Missing required location attribute",
      )
    }.assertAll()
  }

  @Test
  fun `repositions origin due to offset attribute when vertical pipe is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)
    val baseOrigin = pipeBase.origin
    val expectedOrigin = StagePoint(baseOrigin.x - 11.25f + 3f, baseOrigin.y, baseOrigin.z + 40f)
    val xmlElement = minimalXmlWithVerticalPipeParent()
    xmlElement.setAttribute("offset", "3")

    val instance = Pipe.factory(xmlElement, verticalPipe)

    SoftAssertions().apply {
      assertThat(instance.origin).isEqualTo(expectedOrigin)
      assertThat(instance.length).isEqualTo(22.5f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for attributes when vertical pipe is not parent`() {
    val xmlElement = minimalXmlWithNoParent()
    xmlElement.setAttribute("offset", "3")
    xmlElement.setAttribute("offsety", "30")

    val instance = Pipe.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "Without a vertical pipe parent, the offset attribute is not allowed",
        "Unable to offset drawing of pipe when it is not a child of a vertical pipe",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "bogus.1")
    xmlElement.setAttribute("y", "bogus.2")
    xmlElement.setAttribute("z", "bogus.3")
    xmlElement.setAttribute("length", "bogus.4")

    val instance = Pipe.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "Unable to read floating-point number from x attribute",
        "Unable to read floating-point number from y attribute",
        "Unable to read floating-point number from z attribute",
        "Unable to read positive floating-point number from length attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes when vertical pipe is parent`() {
    val xmlElement = minimalXmlWithVerticalPipeParent()
    xmlElement.setAttribute("offset", "three")
    xmlElement.setAttribute("offsety", "thirty")
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)

    val instance = Pipe.factory(xmlElement, pipeBase)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsOnly(
        "Unable to read optional floating-point number from offset attribute",
        "Unable to read optional floating-point number from offsety attribute",
      )
    }.assertAll()
  }

  @Test
  fun `tracks vertical status when there is no parent`() {
    val instance = Pipe.factory(minimalXmlWithNoParent(), null)

    assertThat(instance.vertical).isFalse
  }

  @Test
  fun `tracks vertical status when pipe base is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)

    val instance = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)

    assertThat(instance.vertical).isTrue
  }

  @Test
  fun `tracks vertical status when vertical pipe is parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)

    val instance = Pipe.factory(minimalXmlWithVerticalPipeParent(), verticalPipe)

    assertThat(instance.vertical).isFalse
  }

  @Test
  fun `vertical pipe registers self with pipe base parent`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)

    val instance = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)

    assertThat(pipeBase.upright).isSameAs(instance)
  }

  @Test
  fun `vertical pipe keeps references to dependent pipes`() {
    val pipeBase = PipeBase.factory(PipeBaseTest().minimalXml(), null)
    val verticalPipe = Pipe.factory(minimalXmlWithPipeBaseParent(), pipeBase)

    val instance = Pipe.factory(minimalXmlWithVerticalPipeParent(), verticalPipe)

    assertThat(verticalPipe.dependents).contains(
      Locator( 38f, instance)
    )
  }

  // TODO Does this test really belong with Pipe?
  @Test
  fun `reorient using proscenium coordinates`() {
    Proscenium.instances.clear()
    val prosceniumElement = IIOMetadataNode()
    prosceniumElement.setAttribute("x", "120")
    prosceniumElement.setAttribute("y", "230")
    prosceniumElement.setAttribute("z", "34")
    prosceniumElement.setAttribute("height", "4.5")
    prosceniumElement.setAttribute("width", "5.6")
    prosceniumElement.setAttribute("depth", "6.7")
    assertThat(Proscenium.inUse()).isFalse
    Proscenium.factory(prosceniumElement, null)
    assertThat(Proscenium.inUse()).isTrue
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Pipe.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.origin).isNotEqualTo(StagePoint(130f, 210f, 334f))
      assertThat(instance.origin.venue).isEqualTo(VenuePoint(130f, 210f, 334f))
    }.assertAll()
  }

  @Test
  fun `find pipe by id`() {
    Pipe.instances.clear()
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "name")
    xmlElement.setAttribute("x", "10")
    xmlElement.setAttribute("y", "20")
    xmlElement.setAttribute("z", "300")
    xmlElement.setAttribute("length", "4.5")
    val instance = Pipe.factory(xmlElement, null)

    val foundPipe = Pipe.queryById("name")

    assertThat(foundPipe).isSameAs(instance)
  }

  @Test
  fun `do not find pipe by id`() {
    Pipe.instances.clear()

    val foundPipe = Pipe.queryById("name")

    assertThat(foundPipe).isNull()
  }

  @Test
  fun `find pipe by xml element`() {
    Pipe.instances.clear()
    Luminaire.instances.clear()
    val filename = "pipeAndLuminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup.startup(pathName)
    val luminaireXml = Luminaire.instances[0].xmlElement
    val pipeXml = Pipe.instances[0].xmlElement

    val foundPipe = Pipe.queryByXmlElement(luminaireXml.parentNode as Element)

    SoftAssertions().apply {
      assertThat(foundPipe).isNotNull
      assertThat(foundPipe?.xmlElement).isSameAs(pipeXml)
    }.assertAll()
  }

  @Test
  fun `do not find pipe by xml element`() {
    Pipe.instances.clear()
    Luminaire.instances.clear()
    val filename = "luminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup.startup(pathName)
    val luminaireXml = Luminaire.instances[0].xmlElement

    val foundPipe = Pipe.queryByXmlElement(luminaireXml)

    assertThat(foundPipe).isNull()
  }

  @Test
  fun `keeps track of child entities according to location on pipe`() {
    Pipe.instances.clear()
    val pipeElement = IIOMetadataNode()
    pipeElement.setAttribute("id", "name")
    pipeElement.setAttribute("x", "10")
    pipeElement.setAttribute("y", "20")
    pipeElement.setAttribute("z", "300")
    pipeElement.setAttribute("length", "4.5")
    val pipe = Pipe.factory(pipeElement, null)
    val luminaireElement = IIOMetadataNode()
    luminaireElement.setAttribute("type", "Type value")
    luminaireElement.setAttribute("location", "17.6")
    luminaireElement.setAttribute("owner", "Owner name")
    luminaireElement.setAttribute("address", "124")
    val luminaire = Luminaire.factory(luminaireElement, null)
    val luminaireElement2 = IIOMetadataNode()
    luminaireElement2.setAttribute("type", "Type value")
    luminaireElement2.setAttribute("location", "-17.6")
    luminaireElement2.setAttribute("owner", "Owner name")
    luminaireElement2.setAttribute("address", "124")
    val luminaire2 = Luminaire.factory(luminaireElement2, null)

    pipe.hang(luminaire)
    pipe.hang(luminaire2)

    Pipe.sortDependents()

    assertThat(pipe.dependents).containsExactly(
      Locator(-17.6f, luminaire2),
      Locator(17.6f, luminaire)
    )

  }
}
