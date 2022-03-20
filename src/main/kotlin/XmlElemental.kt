import org.w3c.dom.Element

abstract class XmlElemental(val xmlElement: Element) {
  val errors = mutableListOf<String>()
  val hasError: Boolean
    get() = errors.size > 0

//  val int = Int::class.java

//  fun attributes(attributes: Map<String, Type>){
//
//  }

  fun getStringAttribute(name: String): String {
    val value = xmlElement.getAttribute(name)
    if (value.isEmpty()) {
      errors.add("Missing required $name attribute")
    }
    return value
  }

  fun getOptionalStringAttribute(name: String): String {
    return xmlElement.getAttribute(name)
  }

  fun getPositiveIntegerAttribute(name: String): Int {
    val valueString = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("Missing required $name attribute")
      return 0
    }
    var value = 0
    try {
      value = valueString.toInt()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read positive integer from $name attribute")
    }
    return value
  }

  protected open fun getFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("Missing required $name attribute")
      return 0.0f
    }
    var value = 0.0f
    try {
      value = valueString.toFloat()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read floating-point number from $name attribute")
    }
    return value
  }

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