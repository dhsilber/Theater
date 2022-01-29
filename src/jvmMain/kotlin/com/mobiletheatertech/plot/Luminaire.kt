package com.mobiletheatertech.plot

import org.w3c.dom.Element

class Luminaire(val element: Element) : Elemental() {
  val errors = mutableListOf<String>()
  val hasError: Boolean
    get() = errors.size > 0
  var type: String = getStringAttribute("type")
  var address: Int = getPositiveIntegerAttribute("address")
    set(value) {
      field = value
      element.setAttribute("address", value.toString())
      Xml.write()
    }

  init {
    println("New Luminaire of type $type, with address $address.")
  }

  private fun getStringAttribute(name: String): String {
    val value = element.getAttribute(name)
    if (value.isEmpty()) errors.add("Missing required $name attribute")
    return value
  }

  private fun getPositiveIntegerAttribute(name: String): Int {
    val valueString = element.getAttribute(name)
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

  companion object : CreateWithElement<Luminaire>() {
    const val Tag = "luminaire"
    fun factory(element: Element): Luminaire = create(element, ::Luminaire)
  }

}