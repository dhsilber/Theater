package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Flat(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var start = getStagePointAttribute("x1", "y1")
  var end = getStagePointAttribute( "x2", "y2" )

  companion object : CreateWithXmlElement<Flat>() {
    const val Tag = "flat"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Flat =
      Flat.create(xmlElement, parentEntity, ::Flat)
  }

  override fun toString(): String = "Flat start (${start.x}, ${start.y}) end (${end.x}, ${end.y})"
}