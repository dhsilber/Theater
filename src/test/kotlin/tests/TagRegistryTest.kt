package tests

import TagRegistry
import XmlCompanion
import org.assertj.core.api.JUnitSoftAssertions
import org.assertj.core.api.SoftAssertions
import org.junit.Rule
import org.junit.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class TagRegistryTest {
  class StandIn(override val xmlElement: Element) : XmlCompanion {
    companion object {
      var lastCreated: XmlCompanion? = null

      fun callback(xmlElement: Element) {
        lastCreated = StandIn(xmlElement)
      }
    }
  }

  @Test
  fun `register a tag with an associated callback method`() {
    val tag = "TagRegistryTesttag"
    TagRegistry.tagToCallback.remove(tag)

    TagRegistry.registerConsumer(tag, StandIn.Companion::callback)

    val xmlElement = IIOMetadataNode()
    TagRegistry.registerProvider("wall", xmlElement)
    TagRegistry.registerProvider(tag, xmlElement)
    SoftAssertions().apply {
      assertThat(StandIn.lastCreated).isNotNull
      assertThat(xmlElement).isSameAs(StandIn.lastCreated?.xmlElement)
    }.assertAll()
  }
}
