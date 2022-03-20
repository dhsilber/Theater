package tests

import TagRegistry
import XmlCompanion
import org.junit.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertNotNull
import kotlin.test.assertSame

class TagRegistryTest {

  data class StandIn(override val xmlElement: Element) : XmlCompanion {

    companion object {
      var lastCreated: XmlCompanion? = null

      fun callback(xmlElement: Element) {
        println("--> In callback $xmlElement")
        lastCreated = StandIn(xmlElement)
      }
    }
  }

  @Test
  fun `register a tag and an associated callback method`() {

    val tag = "TagRegistryTesttag"
    TagRegistry.tagToCallback.remove("TagRegistryTesttag")
    println("TagRegistryTest: ${TagRegistry.tagToCallback}")
    TagRegistry.registerConsumer(tag, StandIn.Companion::callback)
    println("TagRegistryTest: ${TagRegistry.tagToCallback}")
    val xmlElement = IIOMetadataNode()
    println("TagRegistryTest: $xmlElement, ${StandIn.lastCreated}")

    TagRegistry.registerProvider("wall", xmlElement)
    TagRegistry.registerProvider(tag, xmlElement)
    println("TagRegistryTest: ${StandIn.lastCreated}")

    assertNotNull(StandIn.lastCreated)
    assertSame(xmlElement, StandIn.lastCreated?.xmlElement)
  }
}