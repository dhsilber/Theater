package entities

import CreateWithXmlElement
import XmlElemental
import coordinates.VenuePoint
import org.w3c.dom.Element

class Venue(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var building = getStringAttribute("building")
  var room = getStringAttribute("room")
  var width = getPositiveIntegerAttribute("width")
  var depth = getPositiveIntegerAttribute("depth")
  var height = getPositiveIntegerAttribute("height")
  var circuiting = getOptionalStringAttribute("circuiting")

  val origin = VenuePoint(0f, 0f, 0f)
  val opposite = VenuePoint(width, depth, height)

  init {
    println("New Venue: $building - $room.")
    println("New Venue: $this.")
  }

  override fun toString() = "$building / $room: ($width, $depth, $height) $circuiting"

  companion object : CreateWithXmlElement<Venue>() {
    const val Tag = "venue"

    fun factory(xmlElement: Element, parentEntity: XmlElemental? = null): Venue =
      create(xmlElement, parentEntity, ::Venue)
  }
}

//<venue
//building="Westin Boston Waterfront"
//room="Grand Ballroom A/B"
//width="1284"
//depth="1210"
//height="239"
//circuiting="one-to-one"
//>
