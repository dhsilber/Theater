package display

import SvgDocument
import coordinates.VenuePoint
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.Float.max
import java.lang.Float.min
import display.DrawingOrderOperation.CIRCLE
import display.DrawingOrderOperation.LINE
import display.DrawingOrderOperation.RECTANGLE

fun makeElementInDocument(svgDocument: SvgDocument, tagName: String): Element {
  val element = svgDocument
    .document
    .createElementNS(svgDocument.namespace, tagName)

  svgDocument.root.appendChild(element)

  return element
}

fun Element.addAttribute(name: String, value: String): Element {
  this.setAttribute(name, value)
  return this
}

fun drawCircle(
  svgDocument: SvgDocument,
  x: Float,
  y: Float,
  r: Float,
): DrawingResults {
  val svgElement = svgDocument.document.createElementNS(svgDocument.namespace, "circle")
  svgElement.setAttribute("cx", x.toString())
  svgElement.setAttribute("cy", y.toString())
  svgElement.setAttribute("r", r.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")
  svgElement.setAttribute("fill", "none")
  svgDocument.root.appendChild(svgElement)

  return DrawingResults(svgElement, SvgBoundary(x - r, y - r, x + r, y + r))
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
  svgDocument: SvgDocument,
  start: VenuePoint,
  end: VenuePoint,
): SvgBoundary {
  val svgElement = makeElementInDocument(svgDocument, "line")
  svgElement.setAttribute("x1", start.x.toString())
  svgElement.setAttribute("y1", start.y.toString())
  svgElement.setAttribute("x2", end.x.toString())
  svgElement.setAttribute("y2", end.y.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")

  return SvgBoundary(
    min(start.x, end.x),
    min(start.y, end.y),
    max(start.x, end.x),
    max(start.y, end.y),
  )
}

fun drawLineWithResults(
  svgDocument: SvgDocument,
  start: VenuePoint,
  end: VenuePoint,
): DrawingResults {
  val svgElement = makeElementInDocument(svgDocument, "line")
  svgElement.setAttribute("x1", start.x.toString())
  svgElement.setAttribute("y1", start.y.toString())
  svgElement.setAttribute("x2", end.x.toString())
  svgElement.setAttribute("y2", end.y.toString())
  svgElement.setAttribute("stroke", "black")
  svgElement.setAttribute("stroke-width", "2")

  return DrawingResults(
    svgElement,
    SvgBoundary(
      min(start.x, end.x),
      min(start.y, end.y),
      max(start.x, end.x),
      max(start.y, end.y),
    )
  )
}

fun drawRectangle(
  svgDocument: SvgDocument,
  x: Float,
  y: Float,
  width: Float,
  height: Float,
  fillColor: String = "white",
  opacity: String = "1",
): DrawingResults {
  val rect = makeElementInDocument(svgDocument, "rect")
  rect.setAttribute("x", x.toString())
  rect.setAttribute("y", y.toString())
  rect.setAttribute("width", width.toString())
  rect.setAttribute("height", height.toString())
  rect.setAttribute("fill", fillColor)
  rect.setAttribute("opacity", opacity)

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
  val svgElement = makeElementInDocument(svgDocument, "use")
  svgElement.setAttribute("xlink:href", "#$type")
  svgElement.setAttribute("x", x.toString())
  svgElement.setAttribute("y", y.toString())
}

data class DrawingResults(
  val element: Element,
  val boundary: SvgBoundary,
)

fun svgDraw(svgDocument: SvgDocument, orders: List<DrawingOrder>) {
  orders.map {
    when (it.operation) {
      CIRCLE -> {
        val result = drawCircle(
          svgDocument = svgDocument,
          x = it.data[0],
          y = it.data[1],
          r = it.data[2],
        )
        result.element.addAttribute("stroke", it.color.svg)
      }
      LINE -> {
        val result = drawLineWithResults(
          svgDocument = svgDocument,
          start = VenuePoint(it.data[0], it.data[1], 0f),
          end = VenuePoint(it.data[2], it.data[3], 0f),
        )
        result.element.addAttribute("stroke", it.color.svg)
      }
      RECTANGLE -> {
        val result = drawRectangle(
          svgDocument = svgDocument,
          x = it.data[0],
          y = it.data[1],
          width = it.data[2],
          height = it.data[3],
//          fillColor = it.color.svg,
        )
        result.element.addAttribute("stroke", it.color.svg)
      }
    }
  }
}