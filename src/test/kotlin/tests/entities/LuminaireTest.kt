package tests.entities

import CreateWithXmlElement
import Hangable
import Xml
import XmlElemental
import Startup
import entities.Luminaire
import entities.Pipe
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

class LuminaireTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val luminaire = Luminaire.factory(xmlElement, null)

    assertIs<XmlElemental>(luminaire)
  }

  @Test
  fun `is hangable`() {
    val xmlElement = IIOMetadataNode()

    val luminaire = Luminaire.factory(xmlElement, null)

    assertIs<Hangable>(luminaire)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Luminaire>>(Luminaire)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Luminaire.Tag).isEqualTo("luminaire")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup().startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Luminaire.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Luminaire>(Luminaire.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Luminaire.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.type).isEqualTo("Type value")
      assertThat(instance.location).isEqualTo(17.6f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `registers optional attributes`() {
    val pipeElement = IIOMetadataNode()
    pipeElement.setAttribute("id", "pipe name")
    pipeElement.setAttribute("x", "1.2")
    pipeElement.setAttribute("y", "2.3")
    pipeElement.setAttribute("z", "3.4")
    pipeElement.setAttribute("length", "4.5")
    Pipe.factory(pipeElement, null)

    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("on", "pipe name")
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("circuit", "circuit id")
    xmlElement.setAttribute("dimmer", "512")
    xmlElement.setAttribute("channel", "99")
    xmlElement.setAttribute("color", "Color name")
    xmlElement.setAttribute("target", "Zone name")
    xmlElement.setAttribute("address", "123")
    xmlElement.setAttribute("info", "Info text")
    xmlElement.setAttribute("label", "Label text")
    xmlElement.setAttribute("rotation", "-60")

    val instance = Luminaire.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.owner).isEqualTo("Owner name")
      assertThat(instance.on).isEqualTo("pipe name")
      assertThat(instance.circuit).isEqualTo("circuit id")
      assertThat(instance.dimmer).isEqualTo(512)
      assertThat(instance.channel).isEqualTo(99)
      assertThat(instance.color).isEqualTo("Color name")
      assertThat(instance.target).isEqualTo("Zone name")
      assertThat(instance.address).isEqualTo(123)
      assertThat(instance.info).isEqualTo("Info text")
      assertThat(instance.label).isEqualTo("Label text")
      assertThat(instance.rotation).isEqualTo(-60f)
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Luminaire.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required type attribute",
        "Missing required location attribute",
      )
    }.assertAll()
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("on", "unavailable pipe")
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "bogus17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("dimmer", "bogus512")
    xmlElement.setAttribute("channel", "bogus99")
    xmlElement.setAttribute("address", "bogus.1")
    xmlElement.setAttribute("rotation", "bogus.3")

    val instance = Luminaire.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Unable to read floating-point number from location attribute",
        "Unable to read positive integer from dimmer attribute",
        "Unable to read positive integer from channel attribute",
        "Unable to read positive integer from address attribute",
        "Unable to read optional floating-point number from rotation attribute",
        "Unable to find pipe \"unavailable pipe\" to hang this on",
      )
    }.assertAll()
  }

  @Test
  fun `registers self with parent pipe`() {
    val pipeElement = IIOMetadataNode()
    pipeElement.setAttribute("id", "pipe name")
    pipeElement.setAttribute("x", "1.2")
    pipeElement.setAttribute("y", "2.3")
    pipeElement.setAttribute("z", "3.4")
    pipeElement.setAttribute("length", "4.5")
    val pipe = Pipe.factory(pipeElement, null)

    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")

    mockkObject(pipe)
    val luminaireSlot = slot<Luminaire>()
    every { pipe.hang(dependant = capture(luminaireSlot)) } returns Unit

    val instance = Luminaire.factory(xmlElement, pipe)

    verify(exactly = 1) { pipe.hang(dependant = instance) }
  }

  @Test
  fun `registers self with linked pipe`() {
    val pipeElement = IIOMetadataNode()
    pipeElement.setAttribute("id", "pipe name")
    pipeElement.setAttribute("x", "1.2")
    pipeElement.setAttribute("y", "2.3")
    pipeElement.setAttribute("z", "3.4")
    pipeElement.setAttribute("length", "4.5")
    val pipe = Pipe.factory(pipeElement, null)

    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("on", "pipe name")
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")

    mockkObject(pipe)
    val luminaireSlot = slot<Luminaire>()
    every { pipe.hang(dependant = capture(luminaireSlot)) } returns Unit

    val instance = Luminaire.factory(xmlElement, null)

    verify(exactly = 1) { pipe.hang(dependant = instance) }
  }

  @Test
  fun `change in address updates xmlElement and saves file`() {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("type", "Type value")
    xmlElement.setAttribute("location", "17.6")
    xmlElement.setAttribute("owner", "Owner name")
    xmlElement.setAttribute("address", "124")
    val instance = Luminaire.factory(xmlElement, null)
    mockkObject(Xml)
    every { Xml.write() } returns Unit

    instance.address = 421

    assertThat(xmlElement.getAttribute("address")).isEqualTo("421")
    verify { Xml.write() }
  }

}
