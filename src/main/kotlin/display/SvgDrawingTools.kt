package display

import SvgDocument
import coordinates.VenuePoint
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

fun makeElementInDocument(svgDocument: SvgDocument, tagName: String): Element {
  val element = svgDocument.document.createElementNS(svgDocument.namespace, tagName)

  svgDocument.root.appendChild(element)

  return element
}

fun Element.addAttribute(name: String, value: String): Element {
  this.setAttribute(name, value)
  return this
}

fun drawCircle(
  svgDocument: Document,
  svgNamespace: String,
  parentElement: Element,
  x: Float,
  y: Float,
  r: Float,
): Element {
  val svgElement = svgDocument.createElementNS(svgNamespace, "circle")
  svgElement.setAttribute("cx", x.toString())
  svgElement.setAttribute("cy", y.toString())
  svgElement.setAttribute("r", r.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")
  svgElement.setAttribute("fill", "none")
  parentElement.appendChild(svgElement)

  return svgElement
}

fun drawLine(
  svgDocument: Document,
  svgNamespace: String,
  parentElement: Element,
  x1: Float,
  y1: Float,
  x2: Float,
  y2: Float,
): Element {
  val svgElement = svgDocument.createElementNS(svgNamespace, "line")
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
  svgDocument: Document,
  svgNamespace: String,
  parentElement: Element,
  start: VenuePoint,
  end: VenuePoint,
): Element {
  val svgElement = svgDocument.createElementNS(svgNamespace, "line")
  svgElement.setAttribute("x1", start.x.toString())
  svgElement.setAttribute("y1", start.y.toString())
  svgElement.setAttribute("x2", end.x.toString())
  svgElement.setAttribute("y2", end.y.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")
  parentElement.appendChild(svgElement)

  return svgElement
}

fun drawRectangle(
  svgDocument: SvgDocument,
  x: Float,
  y: Float,
  width: Float,
  height: Float,
  fillColor: String = "white",
): DrawingResults {
  val rect = makeElementInDocument(svgDocument, "rect")
  rect.setAttribute("x", x.toString())
  rect.setAttribute("y", y.toString())
  rect.setAttribute("width", width.toString())
  rect.setAttribute("height", height.toString())
  rect.setAttribute("fill", fillColor)

  return DrawingResults(rect, SvgBoundary(x, y, x + width, y + height))
}

fun drawSymbol(svgDocument: SvgDocument, name: String, svgNode: Node) {
  val symbol = makeElementInDocument(svgDocument, "symbol")
  symbol.setAttribute("id", name)
  symbol.setAttribute("overflow", "visible")

  val importedSvgNode = svgDocument.document.importNode(svgNode, true)
  symbol.appendChild(importedSvgNode)
}

fun drawUse(
  svgDocument: SvgDocument,
  type: String,
  x: Float,
  y: Float,
) {
  val svgElement = makeElementInDocument(svgDocument,  "use")
  svgElement.setAttribute("xlink:href", "#$type")
  svgElement.setAttribute("x", x.toString())
  svgElement.setAttribute("y", y.toString())
}

data class DrawingResults(
  val element: Element,
  val boundary: SvgBoundary,
)