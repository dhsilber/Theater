package com.mobiletheatertech.plot

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import javax.imageio.metadata.IIOMetadataNode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class WallTest {

  @Test
  fun `is elemental`() {
    val element = IIOMetadataNode()
    val wall = Wall.factory(element)
    assertIs<Elemental>(wall)
  }

  @Test
  fun `companion has factory`() {
    assertIs<CreateWithElement<Wall>>(Wall)
  }

  @Test
  fun `has required attributes`() {
    val element = IIOMetadataNode()
    element.setAttribute("x1", "0.1")
    element.setAttribute("y1", "0.2")
    element.setAttribute("x2", "0.3")
    element.setAttribute("y2", "0.4")

    val instance = Wall.factory(element)

    assertFalse(instance.hasError)
  }

  @Test
  fun `notes error for missing required attributes`() {
    val element = IIOMetadataNode()

    val instance = Wall.factory(element)

    assertTrue(instance.hasError)
    assertEquals(4, instance.errors.size)
    assertEquals("Missing required x1 attribute", instance.errors[0])
    assertEquals("Missing required y1 attribute", instance.errors[1])
    assertEquals("Missing required x2 attribute", instance.errors[2])
    assertEquals("Missing required y2 attribute", instance.errors[3])
  }

  @Test
  fun `notes error for badly specified attributes`() {
    val element = IIOMetadataNode()
    element.setAttribute("x1", "bogus.1")
    element.setAttribute("y1", "bogus.2")
    element.setAttribute("x2", "bogus.3")
    element.setAttribute("y2", "bogus.4")

    val instance = Wall.factory(element)

    assertTrue(instance.hasError)
    assertEquals(4, instance.errors.size)
    assertEquals("Unable to read floating-point number from x1 attribute", instance.errors[0])
    assertEquals("Unable to read floating-point number from y1 attribute", instance.errors[1])
    assertEquals("Unable to read floating-point number from x2 attribute", instance.errors[2])
    assertEquals("Unable to read floating-point number from y2 attribute", instance.errors[3])
  }


}