package display

import SvgDocument
import coordinates.Point
import coordinates.VenuePoint
import entities.Floor
import entities.Luminaire
import entities.LuminaireDefinition
import entities.Pipe
import entities.PipeBase
import entities.Proscenium
import entities.Setpiece
import entities.SetPlatform
import entities.Shape
import entities.Venue
import entities.Wall
import org.w3c.dom.Document
import org.w3c.dom.Element

fun drawSvgPipeDrawing(svgDocument: SvgDocument, pipe: Pipe) {
  for (instance in LuminaireDefinition.instances) {
    instance.drawSvgPlan(svgDocument)
  }
  pipe.drawSvgPlan(svgDocument)
}

fun generateSvgSymbols(svgDocument: SvgDocument) {
  for (instance in LuminaireDefinition.instances) {
    instance.drawSvgPlan(svgDocument)
  }
}

fun drawSvgPlanContent(svgDocument: SvgDocument) {
//  create a viewport here to surround the rest of the svg commands
//      see https://stackoverflow.com/questions/2724415/how-to-resize-an-svg-with-batik-and-display-it

  generateSvgSymbols(svgDocument)

  var boundary = SvgBoundary()

  Proscenium.instances.map { it.drawSvgPlan(svgDocument) }
  Wall.instances.map { boundary += it.drawSvgPlan(svgDocument) }
  Floor.instances.map { it.drawSvgPlan(svgDocument) }
  Setpiece.instances.map { it.drawSvgPlan(svgDocument) }
  PipeBase.instances.map { it.drawSvgPlan(svgDocument) }
  Pipe.instances.map { boundary += it.drawSvgPlan(svgDocument) }

  svgDocument.root.setAttribute("height", (boundary.yMax + 50).toString())
}

fun drawSvgSectionContent(svgDocument: SvgDocument) {
//  create a viewport here to surround the rest of the svg commands
//      see https://stackoverflow.com/questions/2724415/how-to-resize-an-svg-with-batik-and-display-it

  generateSvgSymbols(svgDocument)

//  val (document, svgNamespace, generator, parentElement) = svgDocument

  Venue.instances.first().drawSvgSection(svgDocument)
  Floor.instances.map { it.drawSvgSection(svgDocument) }
//  Proscenium.drawSvgSection()
//  Wall.instances.map { it.drawSvgSection(svgDocument) }
//  Floor.instances.map { it.drawSvgSection(svgDocument) }
//
//  for (instance in Proscenium.instances) {
//    instance.drawSvgPlan(svgDocument)
//  }
//  for (instance in Wall.instances) {
//    instance.drawSvgPlan(document, svgNamespace, parentElement)
//  }
//  for (instance in Floor.instances) {
//    instance.drawSvgPlan(svgDocument)
//  }
//  for (instance in Setpiece.instances) {
//    instance.drawSvgPlan(svgDocument)
//  }
//  for (instance in PipeBase.instances) {
//    instance.drawSvgPlan(svgDocument)
//  }
//  for (instance in Pipe.instances) {
//    instance.drawSvgPlan(svgDocument)
//  }

}

fun LuminaireDefinition.drawSvgPlan(svgDocument: SvgDocument) =
  drawSymbol(svgDocument, name, svgContent)

fun Venue.drawSvgSection(svgDocument: SvgDocument) {
  drawRectangle(svgDocument, 0f, 0f, depth.toFloat(), height.toFloat())
}

fun Proscenium.drawSvgPlan(svgDocument: SvgDocument) {
  val (document, svgNamespace, generator, parentElement) = svgDocument

  drawLine(document, svgNamespace, parentElement, origin.x - 17f, origin.y - 17f, origin.x + 17f, origin.y + 17f)
    .addAttribute("stroke", "cyan")
  drawLine(document, svgNamespace, parentElement, origin.x + 17f, origin.y - 17f, origin.x - 17f, origin.y + 17f)
    .addAttribute("stroke", "cyan")
  drawCircle(svgDocument, origin.x, origin.y, 17f).element
    .addAttribute("stroke", "cyan")

  val startX = origin.x - width / 2
  val startY = origin.y
  val endX = origin.x + width / 2
  val endY = origin.y + depth
  // SR end of proscenium arch
  drawLine(document, svgNamespace, parentElement, startX, startY, startX, endY)
  // SL end of proscenium arch
  drawLine(document, svgNamespace, parentElement, endX, startY, endX, endY)
  // US side of proscenium arch
  drawLine(document, svgNamespace, parentElement, startX, startY, endX, startY)
    .addAttribute("stroke-opacity", "0.3")
  // DS side of proscenium arch
  drawLine(document, svgNamespace, parentElement, startX, endY, endX, endY)
    .addAttribute("stroke-opacity", "0.1")
}

fun Wall.drawSvgPlan(svgDocument: SvgDocument): SvgBoundary {
  return drawLine(svgDocument, start, end)
}

fun Floor.drawSvgPlan(svgDocument: SvgDocument) {
  drawRectangle(svgDocument, surface.x, surface.y, surface.width, surface.depth, "grey", "0.1")
}

fun Floor.drawSvgSection(svgDocument: SvgDocument) {
  val venue = Venue.instances.first()
  val height = venue.height - surface.z
  val originY = venue.depth - surface.y - surface.depth

  drawLine(svgDocument.document, svgDocument.namespace, svgDocument.root,
    originY,
    height,
    originY + surface.depth,
    height
  )
}

fun PipeBase.drawSvgPlan(svgDocument: SvgDocument) {
  val place = origin.venue
  drawCircle(svgDocument, place.x, place.y, 18f)
}

fun Pipe.drawSvgPlan(svgDocument: SvgDocument): SvgBoundary {
  return when (vertical) {
    true -> drawVerticalSvg(svgDocument)
    else -> drawHorizontalSvg(svgDocument)
  }
}

fun Pipe.drawVerticalSvg(svgDocument: SvgDocument): SvgBoundary {
  val place = origin.venue
  val drawingResults = drawCircle(svgDocument, place.x, place.y, Pipe.Diameter)
//  val offsetToCenter = length / 2
//  dependents.forEach {
//    val location = place.x + it.location + offsetToCenter
//    it.luminaire.drawSvgPlan(svgDocument, place, location)
//  }
  return drawingResults.boundary
}

fun Pipe.drawHorizontalSvg(svgDocument: SvgDocument): SvgBoundary {
  val place = origin.venue
  val drawingResults = drawRectangle(svgDocument, place.x, place.y, length, Pipe.Diameter, fillColor = "black")
  val offsetToCenter = length / 2
  dependents.forEach {
    val location = place.x + it.location + offsetToCenter
    (it.hangable as Luminaire).drawSvgPlan(svgDocument, place, location)
  }
  return drawingResults.boundary
}

fun Luminaire.drawSvgPlan(svgDocument: SvgDocument, point: VenuePoint, declaredLocation: Float): SvgBoundary {
  drawUse(svgDocument, type, declaredLocation, point.y)

  val luminaireDefinition = LuminaireDefinition.findByName(type)
  val size = luminaireDefinition?.length?.coerceAtLeast(luminaireDefinition.width) ?: 0f

  return SvgBoundary(declaredLocation - size, point.y - size, declaredLocation + size, point.y + size)
}

fun Setpiece.drawSvgPlan(svgDocument: SvgDocument) {
  for (platform in parts) {
    platform.drawSvgPlan(svgDocument, origin.venue)
  }
}

fun SetPlatform.drawSvgPlan(svgDocument: SvgDocument, placement: VenuePoint) {
  for (shape in shapes) {
    shape.drawSvgPlan(svgDocument, placement + origin)
  }
}

fun Shape.drawSvgPlan(svgDocument: SvgDocument, placement: VenuePoint) {
  val originOffset = Point(placement.x - rectangle.width / 2, placement.y - rectangle.depth / 2, 0f)
  drawRectangle(svgDocument, originOffset.x, originOffset.y, rectangle.width, rectangle.depth)
}

data class Position(
  val x: Float,
  val y: Float,
  val width: Float,
  val height: Float,
)