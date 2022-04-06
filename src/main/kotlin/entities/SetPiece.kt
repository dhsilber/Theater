package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class SetPiece(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val origin = getPointAttribute("x", "y")

  companion object : CreateWithXmlElement<SetPiece>() {
    const val Tag = "setpiece"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): SetPiece =
      create(xmlElement, parentEntity, ::SetPiece)
  }
}