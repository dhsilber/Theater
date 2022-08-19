package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Stair(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val origin = getVenuePointAttribute("x", "y", "z")
  val width = getFloatAttribute("width")
  val steps = getPositiveIntegerAttribute("steps")
  val run = getFloatAttribute("run")
  val rise = getFloatAttribute("rise")

  companion object : CreateWithXmlElement<Stair>() {
    const val Tag = "stair"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Stair =
      Stair.create(xmlElement, parentEntity, ::Stair)

  }

}
