package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element
import kotlin.reflect.typeOf

class Luminaire(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var on = getOptionalStringAttribute("on")
  var type = getStringAttribute("type")
  var location = getFloatAttribute("location")
  var owner = getOptionalStringAttribute("owner")
  var circuit = getOptionalStringAttribute("circuit")
  var dimmer = getOptionalPositiveIntegerAttribute("dimmer")
  var channel = getOptionalPositiveIntegerAttribute("channel")
  var color = getOptionalStringAttribute("color")
  var target = getOptionalStringAttribute("target")
  var address = getOptionalPositiveIntegerAttribute("address")
    set(value) {
      field = value
      xmlElement.setAttribute("address", value.toString())
      Xml.write()
    }
  var info = getOptionalStringAttribute("info")
  var label = getOptionalStringAttribute("label")
  var rotation = getOptionalFloatAttribute("rotation")
  val parentEntity = parentEntity

  fun queryParentPipe(): Pipe? {
    if (null !== parentEntity && parentEntity is Pipe) {
      parentEntity.hang(this)
    }
    var pipe: Pipe? = null
    if (!on.isEmpty()) {
      // find Pipe that has matching name
      pipe = Pipe.queryById(on)
      if (null === pipe) {
        errors.add("Unable to find pipe \"$on\" to hang this on")
      } else {
        pipe.hang(this)
        println("... but found that $pipe will be holding it.")
      }
    }
    return pipe
  }

  init {
    println("New Luminaire of type $type, with location $location, address $address.")
    println("Parent of $type is $parentEntity")
    println("Errors: $errors")

    queryParentPipe()
  }

  companion object : CreateWithXmlElement<Luminaire>() {
    const val Tag = "luminaire"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Luminaire =
      create(xmlElement, parentEntity, ::Luminaire)
  }

}
