package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Drawing(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val id = getStringAttribute("id")
  val filename = getStringAttribute("filename")
  val pipe = getOptionalStringAttribute("pipe")

  val hasPipe
    get() = pipe !== ""

  companion object : CreateWithXmlElement<Drawing>() {
    const val Tag = "drawing"

    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Drawing =
      create(xmlElement, parentEntity, ::Drawing)
  }
}