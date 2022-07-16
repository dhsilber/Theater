package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Event(elementPassthrough: Element, val parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val id = getStringAttribute("id")

  companion object : CreateWithXmlElement<Event>() {
    const val Tag = "event"

    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Event =
      Event.create(xmlElement, parentEntity, ::Event)
  }
}