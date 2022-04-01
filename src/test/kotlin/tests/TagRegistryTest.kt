package tests

import TagRegistry
import XmlElemental
import entities.Venue
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode

class TagRegistryTest {
  class StandIn(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {

    companion object {
      var lastCreated: StandIn? = null

      fun callback(xmlElement: Element, parentEntity: XmlElemental?): XmlElemental {
        lastCreated = StandIn(xmlElement, parentEntity)
        return Venue(xmlElement, null)
      }
    }
  }

  @Test
  fun `register a tag with an associated callback method`() {
    val tag = "TagRegistryTestTag"
    TagRegistry.tagToCallback.remove(tag)

    TagRegistry.registerTagProcessor(tag, StandIn.Companion::callback)

    println("Stored: ${TagRegistry.tagToCallback[tag]}")
    println("Actual: ${StandIn.Companion::callback}")

    val xmlElement = IIOMetadataNode()
    TagRegistry.registerProvider("wall", xmlElement, null)
    TagRegistry.registerProvider(tag, xmlElement, null)
    SoftAssertions().apply {
//      assertThat(TagRegistry.tagToCallback[tag]).isSameAs(StandIn.Companion::callback)
      assertThat(StandIn.lastCreated).isNotNull
      assertThat(StandIn.lastCreated!!.xmlElement).isSameAs(xmlElement)
    }.assertAll()
  }
}
