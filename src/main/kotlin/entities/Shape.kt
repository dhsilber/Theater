package entities

import CreateWithXmlElement
import PointOffset
import XmlElemental
import org.w3c.dom.Element

class Shape(elementPassthrough: Element, val parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  var rectangle = getRectangleAttribute("rectangle")
//  var offsetPoint = PointOffset.ZERO
//  var rectangleWidth = 0f
//  var rectangleDepth = 0f

//  private fun parseRectangleSizes() {
//    val parameterStrings = rectangleText.split("""\s+""".toRegex())
//    val parameters = parameterStrings.map { it.toFloat() }
//    if (4 == parameters.size ) {
//      this.offsetPoint = PointOffset( parameters[0], parameters[1])
//      this.rectangleWidth = parameters[2]
//      this.rectangleDepth = parameters[3]
//    }
//    else {
//      println("Shape with rectangle of $rectangleText is broken")
//    }
//
////    val regex = """([+-]?([0-9]+\.?[0-9]*|\.[0-9]+)) ([+-]?([0-9]+\.?[0-9]*|\.[0-9]+))""".toRegex()
////    val matchResult = regex.find(rectangleText)
////    if (null == matchResult || matchResult.value.isEmpty()) {
////      errors.add("Unable to read width and depth floating-point numbers from rectangle attribute")
////      return
////    }
//
////    val (rectangleWidth, nothing, rectangleDepth) = matchResult.destructured
////    this.rectangleWidth = rectangleWidth.toFloat()
////    this.rectangleDepth = rectangleDepth.toFloat()
//  }

  private fun connectWithParent() {
    if( null !== parentEntity && parentEntity is SetPlatform) {
      parentEntity.adopt(this)
    }
    else {
      errors.add("Shape is not associated with a parent set-platform")
    }
  }

  init {
    connectWithParent()
//    parseRectangleSizes()
    println("Shape: $this - ${rectangle.width}, ${rectangle.depth}, parent: $parentEntity")
  }

  companion object : CreateWithXmlElement<Shape>() {
    const val Tag = "shape"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Shape =
      create(xmlElement, parentEntity, ::Shape)
  }
}
