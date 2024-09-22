package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Floor(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val surface = getPlane()

  init {
      println( this )
  }

  companion object : CreateWithXmlElement<Floor>() {
    const val Tag = "floor"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Floor =
      Floor.create(xmlElement, parentEntity, ::Floor)

    fun levelAt(x: Float, y: Float): Float {
      for (section in instances) {
        val plane = section.surface
        if (plane.x < x
          && x < plane.x + plane.width
          && plane.y < y
          && y < plane.y + plane.depth
        ) {
          return plane.z
        }
      }
      return 0f
    }

  }
}

