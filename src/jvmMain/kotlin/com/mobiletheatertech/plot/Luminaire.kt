package com.mobiletheatertech.plot

import org.w3c.dom.Element

class Luminaire(val element: Element) {
  val errors = mutableListOf<String>()
  val hasError: Boolean
    get() = errors.size > 0
  var type: String = getStringAttribute("type")

  protected fun getStringAttribute(name: String): String {
    val value = element.getAttribute(name)
    if (value.isEmpty()) errors.add("Missing required $name attribute")
    return value
  }

  companion object {

    const val Tag = "luminaire"

    val Instances = mutableListOf<Luminaire>()

    fun factory(element: Element): Luminaire {
      val instance = Luminaire(element)
      Instances.add(instance)
      return instance
    }

  }

}