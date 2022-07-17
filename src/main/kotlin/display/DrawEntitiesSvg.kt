package display

import SvgDocument
import coordinates.Point
import coordinates.VenuePoint
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.Proscenium
import entities.Setpiece
import entities.SetPlatform
import entities.Shape
import entities.Wall
import org.w3c.dom.Document
import org.w3c.dom.Element

fun drawSvgPipeDrawing(svgDocument: SvgDocument, pipe: Pipe) {
  for (instance in LuminaireDefinition.instances) {
    instance.drawSvg(svgDocument)
  }
  pipe.drawSvg(svgDocument)
}

fun generateSvgSymbols(svgDocument: SvgDocument) {
  for (instance in LuminaireDefinition.instances) {
    instance.drawSvg(svgDocument)
  }
}

fun drawSvgPlanContent(svgDocument: SvgDocument) {
//  create a viewport here to surround the rest of the svg commands
//      see https://stackoverflow.com/questions/2724415/how-to-resize-an-svg-with-batik-and-display-it

  generateSvgSymbols(svgDocument)

  val (document, svgNamespace, generator, parentElement) = svgDocument

  for (instance in Proscenium.instances) {
    instance.drawSvg(document, svgNamespace, parentElement)
  }
  for (instance in Wall.instances) {
    instance.drawSvg(document, svgNamespace, parentElement)
  }
  for (instance in Setpiece.instances) {
    instance.drawSvg(svgDocument)
  }
  for (instance in Pipe.instances) {
    instance.drawSvg(svgDocument)
  }

}

fun LuminaireDefinition.drawSvg(svgDocument: SvgDocument) =
  drawSymbol(svgDocument, name, svgContent)

fun Proscenium.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element) {
  drawLine(svgDocument, svgNamespace, parentElement, origin.x - 17f, origin.y - 17f, origin.x + 17f, origin.y + 17f)
    .addAttribute("stroke", "cyan")
  drawLine(svgDocument, svgNamespace, parentElement, origin.x + 17f, origin.y - 17f, origin.x - 17f, origin.y + 17f)
    .addAttribute("stroke", "cyan")
  drawCircle(svgDocument, svgNamespace, parentElement, origin.x, origin.y, 17f)
    .addAttribute("stroke", "cyan")

  val startX = origin.x - width / 2
  val startY = origin.y
  val endX = origin.x + width / 2
  val endY = origin.y + depth
  // SR end of proscenium arch
  drawLine(svgDocument, svgNamespace, parentElement, startX, startY, startX, endY)
  // SL end of proscenium arch
  drawLine(svgDocument, svgNamespace, parentElement, endX, startY, endX, endY)
  // US side of proscenium arch
  drawLine(svgDocument, svgNamespace, parentElement, startX, startY, endX, startY)
    .addAttribute("stroke-opacity", "0.3")
  // DS side of proscenium arch
  drawLine(svgDocument, svgNamespace, parentElement, startX, endY, endX, endY)
    .addAttribute("stroke-opacity", "0.1")
}

fun Wall.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element) {
  drawLine(svgDocument, svgNamespace, parentElement, start, end)
}

fun Pipe.drawSvg(svgDocument: SvgDocument): SvgBoundary {
  val place = origin.venue
  val drawingResults = drawRectangle(svgDocument, place.x, place.y, length, Pipe.Diameter, fillColor = "black")
  val offsetToCenter = length / 2
  dependents.forEach {
    val location = place.x + it.location + offsetToCenter
    it.luminaire.drawSvg(svgDocument, place, location)
  }
  return drawingResults.boundary
}

fun Luminaire.drawSvg(svgDocument: SvgDocument, point: VenuePoint, declaredLocation: Float): SvgBoundary {
  drawUse(svgDocument, type, declaredLocation, point.y)

  val luminaireDefinition = LuminaireDefinition.findByName(type)
  val size = luminaireDefinition?.length?.coerceAtLeast(luminaireDefinition.width) ?: 0f

  return SvgBoundary(declaredLocation - size, point.y - size, declaredLocation + size, point.y + size)
}

fun Setpiece.drawSvg(svgDocument: SvgDocument) {
  for (platform in parts) {
    platform.drawSvg(svgDocument, origin.venue)
  }
}

fun SetPlatform.drawSvg(svgDocument: SvgDocument, placement: VenuePoint) {
  for (shape in shapes) {
    shape.drawSvg(svgDocument, placement + origin)
  }
}

fun Shape.drawSvg(svgDocument: SvgDocument, placement: VenuePoint) {
  val originOffset = Point(placement.x - rectangle.width / 2, placement.y - rectangle.depth / 2, 0f)
  drawRectangle(svgDocument, originOffset.x, originOffset.y, rectangle.width, rectangle.depth)
}

data class Position(
  val x: Float,
  val y: Float,
  val width: Float,
  val height: Float,
)