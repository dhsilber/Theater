package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Pipe(elementPassthrough: Element) : XmlElemental(elementPassthrough) {
  var id = getStringAttribute("id")
  var x = getFloatAttribute("x")
  var y = getFloatAttribute("y")
  var z = getFloatAttribute("z")
  var length = getPositiveFloatAttribute("length")

  init {
    println("New ${this.toString()}")
    println("Errors: $errors")
  }

  override fun toString(): String {
    return "Pipe at ($x, $y, $z) - length: $length."
  }

  companion object : CreateWithXmlElement<Pipe>() {
    const val Tag = "pipe"
    const val Diameter = 2f

    fun factory(xmlElement: Element): Pipe = create(xmlElement, ::Pipe)

    // For use when
    fun reorientForProsceniumOrigin() {
      val proscenium = Proscenium.get()
      Instances.forEach {
        it.x = proscenium.x + it.x
        it.y = proscenium.y - it.y
        it.z = proscenium.z + it.z
      }
    }
  }
}

