package display

import SvgDocument
import coordinates.Point
import coordinates.VenuePoint
import entities.*

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

  Wall.instances.map { boundary += it.drawSvgPlan(svgDocument) }
  Floor.instances.map { svgDraw(svgDocument, it.drawPlan()) }
  Stair.instances.map { boundary += svgDraw(svgDocument, it.drawPlan()) }
  Proscenium.instances.map { boundary += svgDraw(svgDocument, it.drawPlan()) }
  PipeBase.instances.map { svgDraw(svgDocument, it.drawPlan()) }
//  Pipe.instances.map { boundary += it.drawSvgPlan(svgDocument) }
  Pipe.instances.map { boundary += svgDraw(svgDocument, it.drawPlan()) }
  Raceway.instances.map { boundary += svgDraw(svgDocument, it.drawPlan()) }
  Setpiece.instances.map { it.drawSvgPlan(svgDocument) }

  svgDocument.root.setAttribute("height", (boundary.yMax + 50).toString())
}

fun drawSvgSectionContent(svgDocument: SvgDocument) {
//  create a viewport here to surround the rest of the svg commands
//      see https://stackoverflow.com/questions/2724415/how-to-resize-an-svg-with-batik-and-display-it

  generateSvgSymbols(svgDocument)
  var boundary = SvgBoundary()

//  val (document, svgNamespace, generator, parentElement) = svgDocument

  Venue.instances.first().drawSvgSection(svgDocument)
//  Wall.instances.map { it.drawSvgSection(svgDocument) }
  Floor.instances.map { svgDraw(svgDocument, it.drawSection()) }
  Stair.instances.map { boundary += svgDraw(svgDocument, it.drawSection()) }
  Proscenium.instances.map { boundary += svgDraw(svgDocument, it.drawSection()) }
  PipeBase.instances.map { svgDraw(svgDocument, it.drawSection()) }
  Pipe.instances.map { boundary += svgDraw(svgDocument, it.drawSection()) }
  Raceway.instances.map { boundary += svgDraw(svgDocument, it.drawSection()) }
  Setpiece.instances.map { svgDraw(svgDocument, it.drawSection()) }
}

fun LuminaireDefinition.drawSvgPlan(svgDocument: SvgDocument) =
  drawSymbol(svgDocument, name, svgContent)

fun Venue.drawSvgSection(svgDocument: SvgDocument) {
  drawRectangle(svgDocument, 0f, 0f, depth.toFloat(), height.toFloat())
}

fun Wall.drawSvgPlan(svgDocument: SvgDocument): SvgBoundary {
  return drawLine(svgDocument, start, end)
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
  val drawingResults = drawCircle(svgDocument, place.x, place.y, Pipe.Diameter / 2)
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
