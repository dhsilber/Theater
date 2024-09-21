package display

import SvgDocument
import coordinates.VenuePoint
import display.DrawingOrderOperation.*
import entities.LuminaireDefinition
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.Float.max
import java.lang.Float.min
import javax.imageio.metadata.IIOMetadataNode

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
  svgElement.setAttribute("stroke-width", "1")
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
  svgElement.setAttribute("stroke-width", "1")
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
  svgElement.setAttribute("stroke-width", "1")

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
  svgElement.setAttribute("stroke-width", "1")

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

fun pathFromVertices(vertices: List<VenuePoint>): String {
  val path = buildString {
    append("M ").append(vertices[0]).append(" ").append(vertices[1])
      .append(" L ").append(vertices[2]).append(" ").append(vertices[3])
      .append(" L ").append(vertices[4]).append(" ").append(vertices[5])
      .append(" Z")
  }
  return path
}

fun drawPath(
  svgDocument: SvgDocument,
  vertices: List<VenuePoint>,
  fillColor: String = "white",
  opacity: String = "1",
): DrawingResults {
  val path = pathFromVertices(vertices)
  val figure = makeElementInDocument(svgDocument, "path")
  figure.setAttribute("d", path)
  figure.setAttribute("fill", fillColor)
  figure.setAttribute("opacity", opacity)
  return DrawingResults(figure, SvgBoundary())
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
): Element {
  val svgElement = makeElementInDocument(svgDocument, "use")
  svgElement.setAttribute("xlink:href", "#$type")
  svgElement.setAttribute("x", x.toString())
  svgElement.setAttribute("y", y.toString())
  return svgElement
}

data class DrawingResults(
  val element: Element,
  val boundary: SvgBoundary,
)

fun svgDraw(svgDocument: SvgDocument, orders: List<DrawingOrder>): SvgBoundary {
  var boundary = SvgBoundary()

  orders.map {
    val result: DrawingResults = when (it.operation) {
      CIRCLE -> drawCircle(svgDocument, it)
      LINE -> drawLine(svgDocument, it)
      THICK_LINE -> drawLine(svgDocument, it)
      DASHED_LINE -> drawDashedLine(svgDocument, it)
      RECTANGLE -> drawRectangle(svgDocument, it)
      FILLED_RECTANGLE -> drawFilledRectangle(svgDocument, it)
      USE -> drawUse(svgDocument, it)
      FILLED_RIGHT_TRIANGLE -> drawFilledRightTriangle(svgDocument, it)
    }
    boundary += result.boundary
    result.element
  }

  return boundary
}

private fun drawCircle(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
  val result = drawCircle(
    svgDocument = svgDocument,
    x = it.data[0],
    y = it.data[1],
    r = it.data[2],
  )
  result.element.addAttribute("stroke", it.color.svg)
  return result
}

private fun drawLine(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
  val result = drawLineWithResults(
    svgDocument = svgDocument,
    start = VenuePoint(it.data[0], it.data[1], 0f),
    end = VenuePoint(it.data[2], it.data[3], 0f),
  )
  result.element.addAttribute("stroke", it.color.svg)
  if (it.explanation.isNotEmpty()) {
    result.element.setAttribute("explanation", it.explanation)
  }
  return result
}

private fun drawDashedLine(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
  val result = drawLineWithResults(
    svgDocument = svgDocument,
    start = VenuePoint(it.data[0], it.data[1], 0f),
    end = VenuePoint(it.data[2], it.data[3], 0f),
  )
  result.element.addAttribute("stroke", it.color.svg)
  result.element.addAttribute("stroke-dasharray", "3 3")
  if (it.explanation.isNotEmpty()) {
    result.element.setAttribute("explanation", it.explanation)
  }
  return result
}

private fun drawRectangle(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
  val result = drawRectangle(
    svgDocument = svgDocument,
    x = it.data[0],
    y = it.data[1],
    width = it.data[2],
    height = it.data[3],
  )
  result.element.addAttribute("stroke", it.color.svg)
  return result
}

private fun drawFilledRectangle(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
  val result = drawRectangle(
    svgDocument = svgDocument,
    x = it.data[0],
    y = it.data[1],
    width = it.data[2],
    height = it.data[3],
    fillColor = it.color.svg,
    opacity = it.opacity.toString(),
  )
  result.element.addAttribute("stroke", it.color.svg)
  if (it.explanation.isNotEmpty()) {
    result.element.setAttribute("explanation", it.explanation)
  }
  return result
}

private fun drawFilledRightTriangle(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
//  if (it.explanation.isNotEmpty()) {
//    result.element.setAttribute("explanation", it.explanation)
//  }

  return DrawingResults(IIOMetadataNode(), SvgBoundary(0f, 0f, 0f, 0f))
}

private fun drawUse(svgDocument: SvgDocument, it: DrawingOrder): DrawingResults {
  val type = it.useType
  val x = it.data[0]
  val y = it.data[1]
  val element = drawUse(svgDocument, type, x, y)
  val luminaireDefinition = LuminaireDefinition.findByName(type)
  val size = luminaireDefinition?.length?.coerceAtLeast(luminaireDefinition.width) ?: 0f
  val boundary = SvgBoundary(x - size, y - size, x + size, y + size)

  element.setAttribute("transform", "rotate(${180 + it.useOrientation} $x $y)")

  return DrawingResults(element, boundary)
}
