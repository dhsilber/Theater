package entities

import CreateWithXmlElement
import Hangable
import XmlElemental
import org.w3c.dom.Element

class Luminaire(elementPassthrough: Element, val parentEntity: XmlElemental?) :
  XmlElemental(elementPassthrough), Hangable {

  var on = getOptionalStringAttribute("on")
  var type = getStringAttribute("type")
  override var location = getFloatAttribute("location")
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

  private fun queryParentPipe(): Pipe? {
    if (null !== parentEntity && parentEntity is Pipe) {
      parentEntity.hang(this)
    }
    var pipe: Pipe? = null
    if (on.isNotEmpty()) {
      // find Pipe that has matching name
      pipe = Pipe.queryById(on)
      if (null === pipe) {
        errors.add("Unable to find pipe \"$on\" to hang this on")
      } else {
        pipe.hang(this)
      }
    }
    return pipe
  }

  init {
    queryParentPipe()
  }

  companion object : CreateWithXmlElement<Luminaire>() {
    const val Tag = "luminaire"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Luminaire =
      create(xmlElement, parentEntity, ::Luminaire)
  }

}
