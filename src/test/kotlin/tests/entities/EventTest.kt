package tests.entities

import CreateWithXmlElement
import TagRegistry
import Xml
import XmlElemental
import Startup
import entities.Event
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertIs

class EventTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "Name of event")
    return xmlElement
  }

  @AfterTest
  fun cleanup() {
    unmockkObject(Xml)
  }

  @Test
  fun `is xmlElemental`() {
    val xmlElement = IIOMetadataNode()

    val event = Event.factory(xmlElement, null)

    assertIs<XmlElemental>(event)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithXmlElement<Event>>(Event)
  }

  @Test
  fun `companion has tag`() {
    assertThat(Event.Tag).isEqualTo("event")
  }

  @Test
  fun `registered upon startup`() {
    TagRegistry.tagToCallback.clear()
    mockkObject(Xml)
    every { Xml.read(any()) } returns Unit

    Startup.startup("foo")

    assertThat(TagRegistry.tagToCallback).containsKey(Event.Tag)
  }

  @Test
  fun `companion factory builds correct type`() {
    assertIs<Event>(Event.factory(minimalXml(), null))
  }

  @Test
  fun `has required attributes`() {
    val instance = Event.factory(minimalXml(), null)

    SoftAssertions().apply {
      assertThat(instance.id).isEqualTo("Name of event")
      assertThat(instance.hasError).isFalse
    }.assertAll()
  }

  @Test
  fun `notes error for missing required attributes`() {
    val xmlElement = IIOMetadataNode()

    val instance = Event.factory(xmlElement, null)

    SoftAssertions().apply {
      assertThat(instance.hasError).isTrue
      assertThat(instance.errors).containsExactly(
        "Missing required id attribute",
      )
    }.assertAll()
  }

  @Test
  fun `provides the one event instance`() {

  }
}
