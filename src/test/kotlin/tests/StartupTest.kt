package tests

import TagRegistry
import com.mobiletheatertech.plot.Luminaire
import com.mobiletheatertech.plot.Startup
import com.mobiletheatertech.plot.Wall
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StartupTest {

  @Test
  fun `registers Plot objects to get their XML elements`() {

    val filename = "tiny.xml"
    val pathName = this.javaClass.classLoader.getResource(filename).file  //.readText()
//    val fileContent = this.javaClass.classLoader.getResource(filename).readText()
//    println("$pathName: $fileContent")
    TagRegistry.tagToCallback.clear()
    Startup().startup(pathName)
    assertContains(TagRegistry.tagToCallback, Venue.Tag)
    assertContains(TagRegistry.tagToCallback, Luminaire.Tag)
    assertContains(TagRegistry.tagToCallback, Wall.Tag)
    println(TagRegistry.tagToCallback)
    assertEquals(3, TagRegistry.tagToCallback.size)
  }

}