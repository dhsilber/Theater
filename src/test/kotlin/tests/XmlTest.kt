package com.mobiletheatertech.plot.tests

import TagRegistry
import Xml
import com.mobiletheatertech.plot.Backup
import com.mobiletheatertech.plot.Svg
import io.mockk.*
import org.w3c.dom.Element
import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.AfterTest

internal class XmlTest {

  @AfterTest
  fun cleanup() {
    unmockkObject(Backup)
    unmockkObject(TagRegistry)
  }

//  fun resetXmlDom() {
//    val field = Xml.getThis()::class.java.getDeclaredField("dom")
//
//    with (field) {
//      isAccessible=true
//      set(Xml.dom, null)
//    }
//  }
//
//  @Test
//  fun `write with uninitialized Xml dom does nothing`() {
//    mockkObject(Xml.Companion)
//    unmockkObject(Xml.Companion)
//    resetXmlDom()
////    mockkObject(Backup)
//
//    Xml.write()
//  }

  @Test
  fun `read registers root element's tag`() {
    val filename = "tiny.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(TagRegistry)
    val tagCapture = slot<String>()
    val elementCapture = slot<Element>()
    every { TagRegistry.registerProvider(tag = capture(tagCapture), xmlElement = capture(elementCapture)) } returns Unit

    Xml.Companion.read(pathName)

    verify { TagRegistry.registerProvider(tag = "tag", xmlElement = any()) }
  }

  @Test
  fun `read registers all elements`() {
    val filename = "larger.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(TagRegistry)
    val tagCapture = slot<String>()
    val elementCapture = slot<Element>()
    every { TagRegistry.registerProvider(tag = capture(tagCapture), xmlElement = capture(elementCapture)) } returns Unit

    Xml.Companion.read(pathName)

    verifySequence {
      TagRegistry.registerProvider(tag = "root-tag", xmlElement = any())
      TagRegistry.registerProvider(tag = "childless", xmlElement = any())
      TagRegistry.registerProvider(tag = "three-children", xmlElement = any())
      TagRegistry.registerProvider(tag = "child-one", xmlElement = any())
      TagRegistry.registerProvider(tag = "child-two", xmlElement = any())
      TagRegistry.registerProvider(tag = "child-three", xmlElement = any())
      TagRegistry.registerProvider(tag = "childless", xmlElement = any())
    }
  }

  @Test
  fun `read preserves pathname`() {
    val filename = "larger.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Xml.Companion.PathName = ""

    Xml.Companion.read(pathName)

    assertEquals(pathName, Xml.Companion.PathName)
  }

  @Test
  fun `write backs up file`() {
    val filename = "larger.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    Xml.read(pathName)
    mockkObject(Backup)
    val pathnameCapture = slot<String>()
    every { Backup.backup(sourcePathName = capture(pathnameCapture)) } returns Unit

    Xml.write()

    verify { Backup.backup(sourcePathName = any()) }
  }

  @Test
  fun `write saves changed DOM`() {
    val filename = "larger.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(Backup)
    every { Backup.backup(sourcePathName = any()) } returns Unit
    val sourceContents = Files.readString(File(pathName).toPath())
    Xml.Companion.read(pathName)
    Xml.Companion.PathName="${pathName.removeSuffix(".xml")}-modified.xml"
    val rootElement=Xml.dom.documentElement
    assertTrue(sourceContents.contains("<root-tag>"))
    rootElement.setAttribute("addition", "new data")

    Xml.write()

    val resultingContents = Files.readString(File(Xml.Companion.PathName).toPath())
    assertTrue(resultingContents.contains("<root-tag addition=\"new data\">"))
  }

}
