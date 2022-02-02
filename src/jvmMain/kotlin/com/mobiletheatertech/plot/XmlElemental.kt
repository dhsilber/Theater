package com.mobiletheatertech.plot

import org.w3c.dom.Element

abstract class XmlElemental(val xmlElement: Element) {
  val errors = mutableListOf<String>()
  val hasError: Boolean
    get() = errors.size > 0

//  val int = Int::class.java

//  fun attributes(attributes: Map<String, Type>){
//
//  }

  protected open fun getDoubleAttribute(name: String): Double {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("Missing required $name attribute")
      return 0.0
    }
    var value = 0.0
    try {
      value = valueString.toDouble()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read floating-point number from $name attribute")
    }
    return value
  }

}