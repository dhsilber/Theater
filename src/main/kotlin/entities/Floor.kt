package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Floor(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val surface = getHorizontalPlane()

  companion object : CreateWithXmlElement<Floor>() {
    const val Tag = "floor"
    const val Radius = 18f

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Floor =
      Floor.create(xmlElement, parentEntity, ::Floor)

//    fun queryById(id: String): Floor? {
//      Floor.instances.forEach {
//        if (id == it.id) {
//          return it
//        }
//      }
//      return null
//    }

  }
}