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

  fun getBooleanAttribute(name: String): Boolean {
    val valueString = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      return false
    }
    try {
      val value = valueString.toInt()
      if (1 != value) {
        errors.add("Unable to read boolean 1 from $name attribute")
        return false
      }
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read boolean 1 from $name attribute")
      return false
    }
    return true
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
    if (0 > value) {
      errors.add("Unable to read positive integer from $name attribute")
    }
    return value
  }

  fun getOptionalPositiveIntegerAttribute(name: String): Int {
    val valueString = xmlElement.getAttribute(name)
    var value = 0
    try {
      if (!valueString.isEmpty()) {
        value = valueString.toInt()
      }
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read positive integer from $name attribute")
    }
    if (0 > value) {
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

  protected open fun getPointAttribute(xName: String, yName: String): Point {
    val x = getFloatAttribute(xName)
    val y = getFloatAttribute(yName)

    return Point(x, y, 0f)
  }

  protected open fun getOptionalFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    var value = 0.0f
    try {
      if (!valueString.isEmpty()) {
        value = valueString.toFloat()
      }
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read floating-point number from $name attribute")
    }
    return value
  }

  protected open fun getPositiveFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("Missing required $name attribute")
      return 0.0f
    }
    var value = 0.0f
    try {
      value = valueString.toFloat()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read positive floating-point number from $name attribute")
    }
    if (0 > value) {
      errors.add("Unable to read positive floating-point number from $name attribute")
    }
    return value
  }

  protected open fun getOptionalPositiveFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      return 0.0f
    }
    var value = 0.0f
    try {
      value = valueString.toFloat()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read positive floating-point number from $name attribute")
    }
    if (0 > value) {
      errors.add("Unable to read positive floating-point number from $name attribute")
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
      errors.add("Unable to read double number from $name attribute")
    }
    return value
  }

  protected open fun getPositiveDoubleAttribute(name: String): Double {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("Missing required $name attribute")
      return 0.0
    }
    var value = 0.0
    try {
      value = valueString.toDouble()
    } catch (exception: NumberFormatException) {
      errors.add("Unable to read positive double number from $name attribute")
    }
    if (0 > value) {
      errors.add("Unable to read positive double number from $name attribute")
      return 0.0
    }
    return value
  }

}