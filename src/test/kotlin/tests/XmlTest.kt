package tests

import TagRegistry
import Xml
import XmlElemental
import com.mobiletheatertech.plot.Backup
import io.mockk.*
import org.w3c.dom.Element
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class XmlTest {

  @AfterTest
  fun cleanup() {
    unmockkObject(Backup)
    unmockkObject(TagRegistry)
  }

  @Test
  fun `read registers root element's tag`() {
    val filename = "tiny.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(TagRegistry)
    val tagCapture = slot<String>()
    val elementCapture = slot<Element>()
    val entityCapture = slot<XmlElemental>()
    every { TagRegistry.registerProvider(
      tag = capture(tagCapture),
      xmlElement = capture(elementCapture),
      parentEntity = capture(entityCapture)
    ) } returns null

    Xml.Companion.read(pathName)

    verify { TagRegistry.registerProvider(tag = "tag", xmlElement = any(), parentEntity = any()) }
  }

  @Test
  fun `read registers all elements`() {
    val filename = "larger.xml"
    val pathName = this.javaClass.classLoader.getResource(filename)!!.file
    mockkObject(TagRegistry)
    val tagCapture = slot<String>()
    val elementCapture = slot<Element>()
    val entityCapture = slot<XmlElemental>()
    every { TagRegistry.registerProvider(
      tag = capture(tagCapture),
      xmlElement = capture(elementCapture),
      parentEntity = capture(entityCapture)
    ) } returns null

    Xml.Companion.read(pathName)

    verifyOrder {
      TagRegistry.registerProvider(tag = "root-tag", xmlElement = any(), parentEntity = any())
      TagRegistry.registerProvider(tag = "childless", xmlElement = any(), parentEntity = any())
      TagRegistry.registerProvider(tag = "three-children", xmlElement = any(), parentEntity = any())
      TagRegistry.registerProvider(tag = "child-one", xmlElement = any(), parentEntity = any())
      TagRegistry.registerProvider(tag = "child-two", xmlElement = any(), parentEntity = any())
      TagRegistry.registerProvider(tag = "child-three", xmlElement = any(), parentEntity = any())
      TagRegistry.registerProvider(tag = "childless", xmlElement = any(), parentEntity = any())
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
