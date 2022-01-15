package com.mobiletheatertech.plot

import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.w3c.dom.Element
import kotlin.test.Test

@ExtendWith(MockKExtension::class)
internal class ReadTest {

  @Test
  fun `register root element's tag`() {
    val filename = "tiny.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(TagRegistry)
    val tagCapture = slot<String>()
    val elementCapture = slot<Element>()
    every { TagRegistry.registerProvider(tag = capture(tagCapture), element = capture(elementCapture)) } returns Unit

    Read().input(pathName)

    verify { TagRegistry.registerProvider(tag = "tag", element = any()) }
  }

  @Test
  fun `register all elements`() {
    val filename = "larger.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(TagRegistry)
    val tagCapture = slot<String>()
    val elementCapture = slot<Element>()
    every { TagRegistry.registerProvider(tag = capture(tagCapture), element = capture(elementCapture)) } returns Unit

    Read().input(pathName)

    verifySequence {
      TagRegistry.registerProvider(tag = "root-tag", element = any())
      TagRegistry.registerProvider(tag = "childless", element = any())
      TagRegistry.registerProvider(tag = "three-children", element = any())
      TagRegistry.registerProvider(tag = "child-one", element = any())
      TagRegistry.registerProvider(tag = "child-two", element = any())
      TagRegistry.registerProvider(tag = "child-three", element = any())
      TagRegistry.registerProvider(tag = "childless" , element = any())
    }
  }

}
