package entities

import CreateWithXmlElement
import Hangable
import XmlElemental
import coordinates.StagePoint
import org.w3c.dom.Element

class Raceway(elementPassthrough: Element, val parentEntity: XmlElemental?) :
// Later, when I want to attach a Raceway to a Pipe, we'll make it possible to have a Pipe for a parent
//class Raceway(elementPassthrough: Element, val parentEntity: XmlElemental?) :
  XmlElemental(elementPassthrough), Hangable {

  val id = when (parentEntity) {
//    is Raceway -> ""
//    is RacewayBase -> ""
    else -> getStringAttribute("id")
  }

  val length = getPositiveFloatAttribute("length")

  override var location = when (parentEntity) {
      is Raceway -> getPositiveFloatAttribute("location")
    else -> -1f
  }

  val offset = getOffsetAttribute(parentEntity)
  val offsety = getOffsetYAttribute(parentEntity)

  val origin: StagePoint = when (parentEntity) {
//    is Raceway -> StagePoint(
//      parentEntity.origin.x - length / 2f + offset,
//      parentEntity.origin.y,
//      parentEntity.origin.z + location
//    )
//    is RacewayBase -> StagePoint(
//      parentEntity.origin.x,
//      parentEntity.origin.y,
//      parentEntity.origin.z + 2f
//    )
    else -> getStagePointAttribute("x", "y", "z")
  }

//  var end = StagePoint(x + length, y + Raceway.Diameter, z + Raceway.Diameter)

//  val vertical = parentEntity is RacewayBase

  var dependents = mutableSetOf<Locator>()

  init {
    println("New raceway: $this")
    println("Errors: $errors")
//    when (parentEntity) {
//      is RacewayBase -> parentEntity.mount(this)
//      is Raceway -> parentEntity.hang(this)
//    }
  }

  fun hang(dependant: Hangable) {
    dependents.add(Locator(dependant.location, dependant))
  }

  override fun toString(): String {
    return "Raceway $id at (${origin.x}, ${origin.y}, ${origin.z}) - length: $length."
  }

  companion object : CreateWithXmlElement<Raceway>() {
    const val Tag = "raceway"
    const val Width = 3f
    const val Height = 2f

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Raceway =
      create(xmlElement, parentEntity, ::Raceway)

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

    fun queryById(id: String): Raceway? {
      instances.forEach {
        if (id == it.id) {
          return it
        }
      }
      return null
    }

    fun queryByXmlElement(element: Element): Raceway? {
      instances.forEach {
        if (element === it.xmlElement) {
          return it
        }
      }
      return null
    }
  }
}

//data class Locator(val location: Float, val hangable: Hangable)