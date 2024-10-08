import com.mobiletheatertech.plot.exception.InvalidXMLException
import coordinates.Point
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Pipe
import org.w3c.dom.Element
import org.w3c.dom.Node

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
      errors.add("${xmlElement.tagName} missing required $name attribute")
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
        errors.add("${xmlElement.tagName} unable to read boolean 1 from $name attribute")
        return false
      }
    } catch (exception: NumberFormatException) {
      errors.add("${xmlElement.tagName} unable to read boolean 1 from $name attribute")
      return false
    }
    return true
  }

  fun getPositiveIntegerAttribute(name: String): Int {
    val valueString = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("${xmlElement.tagName} missing required $name attribute")
      return 0
    }
    var value = 0
    try {
      value = valueString.toInt()
    } catch (exception: NumberFormatException) {
      errors.add("${xmlElement.tagName} unable to read positive integer from $name attribute")
    }
    if (0 > value) {
      errors.add("${xmlElement.tagName} unable to read positive integer from $name attribute")
    }
    return value
  }

  fun getOptionalPositiveIntegerAttribute(name: String): Int {
    val valueString = xmlElement.getAttribute(name)
    var value = 0
    try {
      if (valueString.isNotEmpty()) {
        value = valueString.toInt()
      }
    } catch (exception: NumberFormatException) {
      errors.add("${xmlElement.tagName} unable to read positive integer from $name attribute")
    }
    if (0 > value) {
      errors.add("${xmlElement.tagName} unable to read positive integer from $name attribute")
    }
    return value
  }

  protected open fun getFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("${xmlElement.nodeName} missing required $name attribute")
      return 0.0f
    }
    var value = 0.0f
    try {
      value = valueString.toFloat()
    } catch (exception: NumberFormatException) {
      errors.add("${xmlElement.nodeName} unable to read floating-point number from $name attribute")
    }
    return value
  }

  fun getPlane(): Surface {
    return Surface(
      x = getFloatAttribute("x"),
      y = getFloatAttribute("y"),
      z = getFloatAttribute("z"),
      zDepth = getOptionalFloatAttribute("z-depth"),
      // "Height at y + depth"
      width = getFloatAttribute("width"),
      depth = getFloatAttribute("depth"),
    )
  }

  protected open fun getVenuePointAttribute(xName: String, yName: String): VenuePoint {
    val x = getFloatAttribute(xName)
    val y = getFloatAttribute(yName)

    return VenuePoint(x, y, 0f)
  }

  protected open fun getVenuePointAttribute(xName: String, yName: String, zName: String): VenuePoint {
    val x = getFloatAttribute(xName)
    val y = getFloatAttribute(yName)
    val z = getFloatAttribute(zName)

    return VenuePoint(x, y, z)
  }

  protected open fun getStagePointAttribute(xName: String, yName: String): StagePoint {
    val x = getFloatAttribute(xName)
    val y = getFloatAttribute(yName)

    return StagePoint(x, y, 0f)
  }

  protected open fun getStagePointAttribute(xName: String, yName: String, zName: String): StagePoint {
    val x = getFloatAttribute(xName)
    val y = getFloatAttribute(yName)
    val z = getFloatAttribute(zName)

    return StagePoint(x, y, z)
  }

//  protected open fun getPointOffsetAttribute(xName: String, yName: String, zName: String = ""): PointOffset {
//    val x = getFloatAttribute(xName)
//    val y = getFloatAttribute(yName)
//    var z = 0f
//    if (zName.isNotEmpty()) {
//      z = getOptionalFloatAttribute(zName)
//    }
//
//    return PointOffset(x, y, z)
//  }

  protected open fun getPointAttribute(xName: String, yName: String, zName: String = ""): Point {
    val x = getFloatAttribute(xName)
    val y = getFloatAttribute(yName)
    var z = 0f
    if (zName.isNotEmpty()) {
      z = getOptionalFloatAttribute(zName)
    }

    return Point(x, y, z)
  }

  protected open fun getOptionalFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    var value = Float.MIN_VALUE
    try {
      if (valueString.isNotEmpty()) {
        value = valueString.toFloat()
      }
    } catch (exception: NumberFormatException) {
      errors.add("${xmlElement.tagName} unable to read optional floating-point number from $name attribute")
    }
    return value
  }

  protected open fun getPositiveFloatAttribute(name: String): Float {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("${xmlElement.tagName} missing required $name attribute")
      return 0.0f
    }
    var value = 0.0f
    try {
      value = valueString.toFloat()
    } catch (exception: NumberFormatException) {
      errors.add("${xmlElement.tagName} unable to read positive floating-point number from $name attribute")
    }
    if (0 > value) {
      errors.add("${xmlElement.tagName} unable to read positive floating-point number from $name attribute")
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
      errors.add("${xmlElement.tagName} unable to read positive floating-point number from $name attribute")
    }
    if (0 > value) {
      errors.add("${xmlElement.tagName} unable to read positive floating-point number from $name attribute")
    }
    return value
  }

  protected open fun getRectangleAttribute(name: String): Rectangle {
    val valueString: String = xmlElement.getAttribute(name)
    if (valueString.isEmpty()) {
      errors.add("${xmlElement.tagName} missing required $name attribute")
      return Rectangle.Empty
    }

    val parameterStrings = valueString.split("""\s+""".toRegex())
    try {
      val parameters = parameterStrings.map { it.toFloat() }
      if (2 == parameters.size) {
        val width = parameters[0]
        val depth = parameters[1]
        return Rectangle(width, depth)
      } else {
        errors.add("${xmlElement.tagName} unable to read rectangle specification from $name attribute containing '$valueString'")
        return Rectangle.Empty
      }
    } catch (exception: Exception) {
      errors.add("${xmlElement.tagName} unable to read rectangle specification from $name attribute containing '$valueString'")
      return Rectangle.Empty
    }
  }

  fun getSvg(): Node {
    val svgList = xmlElement.getElementsByTagName("svg")

    if (svgList.length > 0) {
      return svgList.item(0)
    } else {
      throw InvalidXMLException("luminaire-definition", "svg", "Expected luminaire-definition to have an svg child")
    }
  }

  fun getOffsetAttribute(parentEntity: XmlElemental?): Float {
    val offset = getOptionalFloatAttribute("offset")
    if (Float.MIN_VALUE != offset && parentEntity !is Pipe) {
      errors.add("${xmlElement.tagName} without a vertical pipe parent, the offset attribute is not allowed")
    }
    return offset
  }

  fun getOffsetYAttribute(parentEntity: XmlElemental?): Float {
    val offset = getOptionalFloatAttribute("offsety")
    if (Float.MIN_VALUE != offset && parentEntity !is Pipe) {
      errors.add("${xmlElement.tagName} unable to offset drawing of pipe when it is not a child of a vertical pipe")
    }
    return offset
  }

//  protected open fun getDoubleAttribute(name: String): Double {
//    val valueString: String = xmlElement.getAttribute(name)
//    if (valueString.isEmpty()) {
//      errors.add("Missing required $name attribute")
//      return 0.0
//    }
//    var value = 0.0
//    try {
//      value = valueString.toDouble()
//    } catch (exception: NumberFormatException) {
//      errors.add("${xmlElement.tagName} unable to read double number from $name attribute")
//    }
//    return value
//  }

//  protected open fun getPositiveDoubleAttribute(name: String): Double {
//    val valueString: String = xmlElement.getAttribute(name)
//    if (valueString.isEmpty()) {
//      errors.add("Missing required $name attribute")
//      return 0.0
//    }
//    var value = 0.0
//    try {
//      value = valueString.toDouble()
//    } catch (exception: NumberFormatException) {
//      errors.add("${xmlElement.tagName} unable to read positive double number from $name attribute")
//    }
//    if (0 > value) {
//      errors.add("${xmlElement.tagName} unable to read positive double number from $name attribute")
//      return 0.0
//    }
//    return value
//  }

}