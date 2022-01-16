package com.mobiletheatertech.plot

import org.junit.jupiter.api.Test
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.*

class LuminaireTest {

  @Test
  fun `factory instantiates instance with element`() {
    val element = IIOMetadataNode()
    val existingcount = Luminaire.Instances.size
    val instance = Luminaire.factory(element)
    assertContains(Luminaire.Instances, instance)
    assertEquals(1 + existingcount, Luminaire.Instances.size)
    assertSame(element, instance.element)
  }

  @Test
  fun `must have type attribute`() {
    val element = IIOMetadataNode()
    element.setAttribute("type", "Type value")
    val instance = Luminaire.factory(element)

    assertEquals("Type value", instance.type)
    assertFalse(instance.hasError)
  }

  @Test
  fun `set error state when no type attribute`() {
    val element = IIOMetadataNode()
    val instance = Luminaire.factory(element)

    assertTrue(instance.hasError)
    assertEquals(1, instance.errors.size)
    assertEquals("Missing required type attribute", instance.errors[0])
  }

}