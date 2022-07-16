package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Shape(elementPassthrough: Element, val parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var rectangle = getRectangleAttribute("rectangle")

  private fun connectWithParent() {
    if( null !== parentEntity && parentEntity is SetPlatform) {
      parentEntity.adopt(this)
    }
    else {
      errors.add("Shape is not associated with a parent set-platform")
    }
  }

  init {
    connectWithParent()
  }

  companion object : CreateWithXmlElement<Shape>() {
    const val Tag = "shape"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Shape =
      create(xmlElement, parentEntity, ::Shape)
  }
}
