package entities

import CreateWithXmlElement
import Hangable
import XmlElemental
import coordinates.StagePoint
import org.w3c.dom.Element

class Pipe(elementPassthrough: Element, val parentEntity: XmlElemental?) :
  XmlElemental(elementPassthrough), Hangable {

  val id = when (parentEntity) {
    is Pipe -> ""
    is PipeBase -> ""
    else -> getStringAttribute("id")
  }

  val length = getPositiveFloatAttribute("length")

  override var location = when (parentEntity) {
    is Pipe -> getPositiveFloatAttribute("location")
    else -> -1f
  }

  val offset = getOffsetAttribute(parentEntity)
  val offsety = getOffsetYAttribute(parentEntity)

  val origin: StagePoint = when (parentEntity) {
    is Pipe -> StagePoint(
      parentEntity.origin.x - length / 2f + offset,
      parentEntity.origin.y,
      parentEntity.origin.z + location
    )
    is PipeBase -> StagePoint(
      parentEntity.origin.x,
      parentEntity.origin.y,
      parentEntity.origin.z + 2f
    )
    else -> getStagePointAttribute("x", "y", "z")
  }

//  var end = StagePoint(x + length, y + Pipe.Diameter, z + Pipe.Diameter)

  val vertical = parentEntity is PipeBase

  var dependents = mutableSetOf<Locator>()

  init {
    println("New $this")
    println("Errors: $errors")
    when (parentEntity) {
      is PipeBase -> parentEntity.mount(this)
      is Pipe -> parentEntity.hang(this)
    }
  }

  fun hang(dependant: Hangable) {
    dependents.add(Locator(dependant.location, dependant))
  }

  override fun toString(): String {
    return "Pipe $id at (${origin.x}, ${origin.y}, ${origin.z}) - length: $length."
  }

  companion object : CreateWithXmlElement<Pipe>() {
    const val Tag = "pipe"
    const val Diameter = 2f

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Pipe =
      create(xmlElement, parentEntity, ::Pipe)

    fun postParsingCleanup() {
      if (Proscenium.inUse()) {
//        reorientForProsceniumOrigin()
      }
      sortDependents()
    }

//    fun reorientForProsceniumOrigin() {
//      val proscenium = Proscenium.get()
//      instances.forEach {
//        it.x += proscenium.x
//        it.y = proscenium.y - it.y
//        it.z += proscenium.z
//        it.origin = StagePoint(it.x, it.y, it.z)
////        it.end = StagePoint(it.x + it.length, it.y + Diameter, it.z + Diameter)
//      }
//    }

    fun sortDependents() {
      instances.forEach {
        it.dependents = it.dependents.toSortedSet { locator1, locator2 ->
          locator1.location.compareTo(locator2.location)
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

data class Locator(val location: Float, val hangable: Hangable)