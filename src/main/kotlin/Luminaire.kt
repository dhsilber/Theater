package com.mobiletheatertech.plot

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Element

class Luminaire(val elementPassthrough: Element) : XmlElemental(elementPassthrough) {
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

  private fun getStringAttribute(name: String): String {
    val value = xmlElement.getAttribute(name)
    if (value.isEmpty()) errors.add("Missing required $name attribute")
    return value
  }

  private fun getPositiveIntegerAttribute(name: String): Int {
    val valueString = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("Missing required $name attribute")
      return 0
    }
    var value = 0
    try {
      value = valueString.toInt()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read number from $name attribute")
    }
    return value
  }

  companion object : CreateWithXmlElement<Luminaire>() {
    const val Tag = "luminaire"
    fun factory(xmlElement: Element): Luminaire = create(xmlElement, ::Luminaire)
  }

}