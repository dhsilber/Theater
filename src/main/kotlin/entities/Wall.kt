package entities

import CreateWithXmlElement
import XmlElemental
import org.w3c.dom.Document
import org.w3c.dom.Element

class Wall(elementPassthrough: Element, parentEntity: XmlElemental?) : XmlElemental(elementPassthrough) {
  val x1 = getFloatAttribute("x1")
  val y1 = getFloatAttribute("y1")
  val x2 = getFloatAttribute("x2")
  val y2 = getFloatAttribute("y2")

  init {
    println("New wall from $x1,$y1 to $x2,$y2.")
  }

//  var start = Point( x1, y1, 0.0 )
//  var end = Point( x2, y2, 0.0 )

  fun svg(svgDocuemnt: Document, svgNamespace: String, parentElement: Element) {
  }

//  //  @Composable
//  fun draw(): Line = Line(x1, y1, x2, y2)
//
//  fun shape() = GenericShape { _, _ ->
//    moveTo(x1, y1)
//    lineTo(x2, y2)
//  }

  override fun toString(): String {
    return "Wall from ($x1, $y1) to ($x2, $y2)."
  }

  companion object : CreateWithXmlElement<Wall>() {
    const val Tag = "wall"
    fun factory(xmlElement: Element, parentEntity: XmlElemental?): Wall =
      create(xmlElement, parentEntity, ::Wall)

    fun createNew(x1: Float, y1: Float, x2: Float, y2: Float) {
      val element = Xml.dom.createElement("wall")
      element.setAttribute("x1", x1.toString())
      element.setAttribute("y1", y1.toString())
      element.setAttribute("x2", x2.toString())
      element.setAttribute("y2", y2.toString())
      element.setAttribute("stroke", "black")
      element.setAttribute("stroke-width", "2");
      Xml.dom.documentElement.appendChild(element)

      factory(element, null)
    }
  }

}