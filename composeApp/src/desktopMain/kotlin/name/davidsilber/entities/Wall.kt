package entities

import CreateWithXmlElement
import coordinates.VenuePoint
import XmlElemental
import org.w3c.dom.Element

class Wall(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var start = getVenuePointAttribute("x1", "y1")
  var end = getVenuePointAttribute( "x2", "y2" )

  init {
    println("New wall from $start to $end.")
    println("Extants: $VenuePoint")
  }

  override fun toString(): String {
    return "Wall from $start to $end."
  }

  companion object : CreateWithXmlElement<Wall>() {
    const val Tag = "wall"

    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Wall =
      create(xmlElement, parentEntity, ::Wall)

    fun createNew(x1: Float, y1: Float, x2: Float, y2: Float) {
      val element = Xml.dom.createElement("wall")
      element.setAttribute("x1", x1.toString())
      element.setAttribute("y1", y1.toString())
      element.setAttribute("x2", x2.toString())
      element.setAttribute("y2", y2.toString())
      element.setAttribute("stroke", "black")
      element.setAttribute("stroke-width", "2");
      Xml.dom.documentElement.appendChild(element)

      factory(element, null)
    }
  }

}