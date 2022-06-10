package entities

import CreateWithXmlElement
import XmlElemental
import coordinates.VenuePoint
import org.w3c.dom.Element

class Proscenium(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  private var x = getFloatAttribute("x")
  private var y = getFloatAttribute("y")
  private var z = getFloatAttribute("z")
  var height = getPositiveFloatAttribute("height")
  var width = getPositiveFloatAttribute("width")
  var depth = getPositiveFloatAttribute("depth")
  var origin = VenuePoint(x, y, z)

  init {
    println("New ${this.toString()}")
    println("Errors: $errors")
  }

  override fun toString(): String {
    return "Proscenium at ($x, $y, $z) - width: $width, depth: $depth, height: $height."
  }

  companion object : CreateWithXmlElement<Proscenium>() {
    const val Tag = "proscenium"

    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Proscenium =
      create(xmlElement, parentEntity, ::Proscenium)

    fun inUse(): Boolean {
      return instances.size > 0
    }

    fun get(): Proscenium {
      return instances.get(0)
    }
  }
}

//<proscenium x="385" y="337" z="40" height="186" width="456" depth="8">
//  <moulding depth="8" width="5" side="both" />
//</proscenium>