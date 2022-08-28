package entities

import CreateWithXmlElement
import XmlElemental
import coordinates.VenuePoint
import org.w3c.dom.Element

class Proscenium(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  private val x = getFloatAttribute("x")
  private val y = getFloatAttribute("y")
  private val z = getFloatAttribute("z")
  val height = getPositiveFloatAttribute("height")
  val width = getPositiveFloatAttribute("width")
  val depth = getPositiveFloatAttribute("depth")
  val origin = VenuePoint(x, y, z)

  override fun toString(): String {
    return "Proscenium at ($x, $y, $z) - width: $width, depth: $depth, height: $height."
  }

  companion object : CreateWithXmlElement<Proscenium>() {
    const val Tag = "proscenium"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Proscenium =
      create(xmlElement, parentEntity, ::Proscenium)

    fun inUse(): Boolean {
      return instances.size > 0
    }

    fun get(): Proscenium {
      return instances[0]
    }

  }

}

//<proscenium x="385" y="337" z="40" height="186" width="456" depth="8">
//  <moulding depth="8" width="5" side="both" />
//</proscenium>