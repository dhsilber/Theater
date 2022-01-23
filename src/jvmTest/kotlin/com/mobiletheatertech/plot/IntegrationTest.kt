package com.mobiletheatertech.plot

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntegrationTest {

  @Test
  fun `read XML with luminaire tag - read, change, write attributes`() {
    TagRegistry.registerConsumer(Luminaire.Tag, Luminaire::factory)
    val existingCount = Luminaire.Instances.size
    val filename = "luminaire.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Startup().startup(pathName)

    assertEquals(3 + existingCount, Luminaire.Instances.size)
    val zero  = Luminaire.Instances[0 + existingCount]
    val one  = Luminaire.Instances[1 + existingCount]
    val two  = Luminaire.Instances[2 + existingCount]
    assertFalse(zero.hasError)
    assertFalse(one.hasError)
    assertTrue(two.hasError)

  }

}
