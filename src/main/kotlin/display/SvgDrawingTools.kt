package display

import VenuePoint
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

fun drawLine(
  svgDocuemnt: Document,
  svgNamespace: String,
  parentElement: Element,
  start: VenuePoint,
  end: VenuePoint
): Element {
  val svgElement = svgDocuemnt.createElementNS(svgNamespace, "line")
  svgElement.setAttribute("x1", start.x.toString())
  svgElement.setAttribute("y1", start.y.toString())
  svgElement.setAttribute("x2", end.x.toString())
  svgElement.setAttribute("y2", end.y.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")
  parentElement.appendChild(svgElement)

  return svgElement
}

fun drawCircle(
  svgDocuemnt: Document,
  svgNamespace: String,
  parentElement: Element,
  x: Float,
  y: Float,
  r: Float
): Element {
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

fun drawRectangle(
  svgDocuemnt: Document,
  svgNamespace: String,
  parentElement: Element,
  x1: Float,
  y1: Float,
  x2: Float,
  y2: Float
): Element {
  val svgElement = svgDocuemnt.createElementNS(svgNamespace, "g")
  svgElement.appendChild(drawLine(svgDocuemnt, svgNamespace, parentElement, x1, y1, x1, y2))
  svgElement.appendChild(drawLine(svgDocuemnt, svgNamespace, parentElement, x1, y2, x2, y2))
  svgElement.appendChild(drawLine(svgDocuemnt, svgNamespace, parentElement, x2, y2, x2, y1))
  svgElement.appendChild(drawLine(svgDocuemnt, svgNamespace, parentElement, x2, y1, x1, y1))
  parentElement.appendChild(svgElement)

  return svgElement
}


fun Element.addAttribute(name: String, value: String): Element {
  this.setAttribute(name, value)
  return this
}


//}