package com.mobiletheatertech.plot

import kotlin.test.Test
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify

internal class ReadTest {

    @Test
    fun `register a tag with its handler class`() {
        val filename = "tiny.xml"
        val pathName = this.javaClass.classLoader.getResource(filename).file
        mockkObject(TagRegistry)
        val tagCapture = slot<String>()
        every { TagRegistry.register(tag = capture(tagCapture)) } returns Unit

        Read().input(pathName)

        verify { TagRegistry.register(tag = "tag") }
    }
}
