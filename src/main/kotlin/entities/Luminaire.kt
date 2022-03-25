package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Luminaire(elementPassthrough: Element) : XmlElemental(elementPassthrough) {
//  val errors = mutableListOf<String>()
//  val hasError: Boolean
//    get() = errors.size > 0
  var type: String = getStringAttribute("type")
  var address: Int = getPositiveIntegerAttribute("address")
    set(value) {
      field = value
      xmlElement.setAttribute("address", value.toString())
      Xml.write()
    }

  init {
    println("New Luminaire of type $type, with address $address.")
  }

  companion object : CreateWithXmlElement<Luminaire>() {
    const val Tag = "luminaire"
    fun factory(xmlElement: Element): Luminaire = create(xmlElement, ::Luminaire)
  }

}