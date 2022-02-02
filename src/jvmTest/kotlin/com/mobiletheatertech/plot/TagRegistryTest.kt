package com.mobiletheatertech.plot

import org.junit.jupiter.api.Test
import org.w3c.dom.Element
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertSame

class TagRegistryTest {

  data class StandIn(override val xmlElement: Element) : XmlCompanion {

    companion object {
      var lastCreated: XmlCompanion? = null

      fun callback(xmlElement: Element) {
        lastCreated = StandIn(xmlElement)
      }
    }
  }

  @Test
  fun `register a tag and an associated callback method`() {

    val tag = "TagRegistryTest tag"
    TagRegistry.registerConsumer(tag, StandIn::callback)
    val xmlElement = IIOMetadataNode()

    TagRegistry.registerProvider(tag, xmlElement)

    assertSame(xmlElement, StandIn.lastCreated?.xmlElement)
  }
}