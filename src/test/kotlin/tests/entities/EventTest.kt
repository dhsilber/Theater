package tests.entities

import CreateWithXmlElement
import XmlElemental
import entities.Event
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.Test
import kotlin.test.assertIs

class EventTest {

  fun minimalXml(): IIOMetadataNode {
    val xmlElement = IIOMetadataNode()
    xmlElement.setAttribute("id", "Name of event")
    return xmlElement
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
    Assertions.assertThat(Event.Tag).isEqualTo("event")
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
