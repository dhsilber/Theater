import com.mobiletheatertech.plot.Backup
import com.mobiletheatertech.plot.exception.InvalidXMLException
import coordinates.VenuePoint
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Node.ELEMENT_NODE
import org.xml.sax.SAXException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class Xml {
  companion object {
    lateinit var PathName: String

    lateinit var dom: Document

    fun getThis(): Xml.Companion {
      return this
    }

    fun read(pathName: String) {
      PathName = pathName

      val incoming: java.io.File = java.io.File(pathName)

      val builderFactory = DocumentBuilderFactory.newInstance()
      var builder: DocumentBuilder? = null
      try {
        builder = builderFactory.newDocumentBuilder()
      } catch (e: ParserConfigurationException) {
        e.printStackTrace()
        System.exit(1)
      }

      try {
        dom = builder!!.parse(incoming)
      } catch (ex: IOException) {
        throw InvalidXMLException("IOException in parsing Plot XML description.")
      } catch (ex: SAXException) {
        throw InvalidXMLException("SAXException in parsing Plot XML description.")
      }

      val root = dom.documentElement

      parse(root, null)
    }

    fun write() {
      if (!::dom.isInitialized)
        return

      Backup.backup(PathName)

//      https://stackoverflow.com/questions/2453105/writing-to-a-xml-file-in-java
      val transformerFactory = TransformerFactory.newInstance()
      val transformer: Transformer = transformerFactory.newTransformer()
      val source = DOMSource(dom)

      val result = StreamResult(StringWriter())

      //t.setParameter(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes")
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
      transformer.transform(source, result)

      //writing to file
      var fop: FileOutputStream? = null
      val file: File
      try {
        file = File(PathName)
        fop = FileOutputStream(file)

        // if file doesnt exists, then create it
        if (!file.exists()) {
          file.createNewFile()
        }

        // get the content in bytes
        val xmlString = result.writer.toString()
        val contentInBytes = xmlString.toByteArray()
        fop.write(contentInBytes)
        fop.flush()
        fop.close()
      } catch (e: IOException) {
        e.printStackTrace()
      } finally {
        try {
          fop?.close()
        } catch (e: IOException) {
          e.printStackTrace()
        }
      }

    }

    fun parse(node: Node, parentEntity: XmlElemental?) {
      if (node.nodeType == ELEMENT_NODE) {
        val xmlElement = node as Element
        val tag = xmlElement.tagName
        val entity = TagRegistry.registerProvider(tag, xmlElement, parentEntity)

        println("After reading $entity, VenuePoint extremes are: ${VenuePoint}")

        var child = xmlElement.firstChild
        while (child != null) {
          parse(child, entity)
          child = child.nextSibling
        }
      }
    }

  }

}

