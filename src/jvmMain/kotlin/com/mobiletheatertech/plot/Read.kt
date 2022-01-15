package com.mobiletheatertech.plot

import com.mobiletheatertech.plot.exception.InvalidXMLException
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Node.ELEMENT_NODE
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

  fun parse(node: Node) {
    if (node.nodeType == ELEMENT_NODE) {
      val element = node as Element
      val tag = element.tagName
      TagRegistry.registerProvider(tag, element)

      var child = element.firstChild
      while (child != null) {
        parse(child)
        child = child.nextSibling
      }
    }
  }

}

