package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class SetPlatform(elementPassthrough: Element, val parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val origin = getStagePointAttribute("x", "y", "z")
  var shapes = mutableListOf<Shape>()

  fun adopt(dependant: Shape) {
    shapes.add(dependant)
  }

  private fun connectWithParent() {
    if( null !== parentEntity && parentEntity is SetPiece) {
      parentEntity.adopt(this)
    }
    else {
      errors.add("Set-platform is not associated with a parent setpiece")
    }
  }

  init {
    connectWithParent()
  }

  companion object : CreateWithXmlElement<SetPlatform>() {
    const val Tag = "set-platform"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): SetPlatform =
      create(xmlElement, parentEntity, ::SetPlatform)
  }
}
