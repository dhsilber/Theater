package com.mobiletheatertech.plot

import org.junit.jupiter.api.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertSame

class TagRegistryTest {

  data class StandIn(override val element: Element) : XmlCompanion {

    companion object {
      var lastCreated: XmlCompanion? = null

      fun callback(element: Element) {
        lastCreated = StandIn(element)
      }
    }
  }

  @Test
  fun `register a tag and an associated callback method`() {

    val tag = "TagRegistryTest tag"
    TagRegistry.registerConsumer(tag, StandIn::callback)
    val element = IIOMetadataNode()

    TagRegistry.registerProvider(tag, element)

    assertSame(element, StandIn.Companion.lastCreated?.element)
  }
}