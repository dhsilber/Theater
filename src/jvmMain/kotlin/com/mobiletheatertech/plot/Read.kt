package com.mobiletheatertech.plot

import com.mobiletheatertech.plot.exception.InvalidXMLException
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class Read {

  fun input(pathName: String) {

    val incoming: java.io.File = java.io.File(pathName)

    val builderFactory = DocumentBuilderFactory.newInstance()
    var builder: DocumentBuilder? = null
    try {
      builder = builderFactory.newDocumentBuilder()
    } catch (e: ParserConfigurationException) {
      e.printStackTrace()
      System.exit(1)
    }

    val xml: Document
    try {
      xml = builder!!.parse(incoming)
    } catch (ex: IOException) {
      throw InvalidXMLException("IOException in parsing Plot XML description.")
    } catch (ex: SAXException) {
      throw InvalidXMLException("SAXException in parsing Plot XML description.")
    }

    val root = xml.documentElement

    parse(root)

  }

  fun parse(element: Element) {
      val tag = element.tagName
      TagRegistry.register(tag)
  }

}

