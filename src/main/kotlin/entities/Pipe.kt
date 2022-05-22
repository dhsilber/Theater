package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Pipe(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var id = getStringAttribute("id")
  var x = getFloatAttribute("x")
  var y = getFloatAttribute("y")
  var z = getFloatAttribute("z")
  var length = getPositiveFloatAttribute("length")

  var dependents = mutableSetOf<Locator>()

  init {
    println("New $this")
    println("Errors: $errors")
  }

  fun hang(dependant: Luminaire) {
    dependents.add(Locator(dependant.location, dependant))
  }

  override fun toString(): String {
    return "Pipe $id at ($x, $y, $z) - length: $length."
  }

  companion object : CreateWithXmlElement<Pipe>() {
    const val Tag = "pipe"
    const val Diameter = 2f

    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Pipe =
      create(xmlElement, parentEntity, ::Pipe)

    fun postParsingCleanup() {
      if (Proscenium.inUse()) {
        reorientForProsceniumOrigin()
      }
      sortDependents()
    }

    fun reorientForProsceniumOrigin() {
      val proscenium = Proscenium.get()
      instances.forEach {
        it.x += proscenium.x
        it.y = proscenium.y - it.y
        it.z += proscenium.z
      }
    }

    fun sortDependents() {
      instances.forEach {
        it.dependents = it.dependents.toSortedSet {
            locator1, locator2 -> locator1.location.compareTo(locator2.location)
        }
      }
    }

    fun queryById(id: String): Pipe? {
      instances.forEach {
        if (id == it.id) {
          return it
        }
      }
      return null
    }

    fun queryByXmlElement(element: Element): Pipe? {
      instances.forEach {
        if (element === it.xmlElement) {
          return it
        }
      }
      return null
    }
  }
}

data class Locator(val location: Float, val luminaire: Luminaire)