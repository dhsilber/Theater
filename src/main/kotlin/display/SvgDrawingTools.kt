package display

import org.w3c.dom.Document
import org.w3c.dom.Element

//class SvgDrawingTools {

fun drawLine(
  svgDocuemnt: Document,
  svgNamespace: String,
  parentElement: Element,
  x1: Float,
  y1: Float,
  x2: Float,
  y2: Float
): Element {
  val svgElement = svgDocuemnt.createElementNS(svgNamespace, "line")
  svgElement.setAttribute("x1", x1.toString())
  svgElement.setAttribute("y1", y1.toString())
  svgElement.setAttribute("x2", x2.toString())
  svgElement.setAttribute("y2", y2.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")
  parentElement.appendChild(svgElement)

  return svgElement
}

fun drawCircle(svgDocuemnt: Document, svgNamespace: String, parentElement: Element, x: Float, y: Float, r: Float):Element {
  val svgElement = svgDocuemnt.createElementNS(svgNamespace, "circle")
  svgElement.setAttribute("cx", x.toString())
  svgElement.setAttribute("cy", y.toString())
  svgElement.setAttribute("r", r.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")
  svgElement.setAttribute("fill", "none")
  parentElement.appendChild(svgElement)

  return svgElement
}

fun Element.addAttribute(name:String, value:String) : Element {
  this.setAttribute(name,value)
  return this
}


//}