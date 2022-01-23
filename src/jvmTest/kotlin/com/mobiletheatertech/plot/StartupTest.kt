package com.mobiletheatertech.plot

import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StartupTest {

  @Test
  fun `registers Plot objects to get their XML elements`() {

    val filename = "tiny.xml"
    val pathName = this.javaClass.classLoader.getResource(filename).file  //.readText()
//    val fileContent = this.javaClass.classLoader.getResource(filename).readText()
//    println("$pathName: $fileContent")
    Startup().startup(pathName)
    assertContains(TagRegistry.tagToCallback, Luminaire.Tag )
    assertEquals(1, TagRegistry.tagToCallback.size)
  }

}
