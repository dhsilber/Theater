package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Setpiece(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val origin = getStagePointAttribute("x", "y")
  val parts = mutableSetOf<SetPlatform>()

  fun adopt(dependant: SetPlatform) {
    parts.add(dependant)
  }

  init {
    println("SetPiece: $this")
  }

  companion object : CreateWithXmlElement<Setpiece>() {
    const val Tag = "setpiece"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Setpiece =
      create(xmlElement, parentEntity, ::Setpiece)
  }
}
