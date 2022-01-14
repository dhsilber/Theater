package com.mobiletheatertech.plot

import io.mockk.*
import kotlin.test.Test

internal class ReadTest {

    @Test
    fun `register root element's tag`() {
        val filename = "tiny.xml"
        val pathName = this.javaClass.classLoader.getResource(filename).file
        mockkObject(TagRegistry)
        val tagCapture = slot<String>()
        every { TagRegistry.register(tag = capture(tagCapture)) } returns Unit

        Read().input(pathName)

        verify { TagRegistry.register(tag = "tag") }
    }

    @Test
    fun `register all elements`() {
        val filename = "larger.xml"
        val pathName = this.javaClass.classLoader.getResource(filename).file
        mockkObject(TagRegistry)
        val tagCapture = slot<String>()
        every { TagRegistry.register(tag = capture(tagCapture)) } returns Unit

        Read().input(pathName)

        verifySequence {
            TagRegistry.register(tag = "root-tag")
            TagRegistry.register(tag = "childless")
            TagRegistry.register(tag = "three-children")
            TagRegistry.register(tag = "child-one")
            TagRegistry.register(tag = "child-two")
            TagRegistry.register(tag = "child-three")
            TagRegistry.register(tag = "childless")
        }
    }
}
