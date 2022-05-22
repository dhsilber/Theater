package display

import Point
import VenuePoint
import androidx.compose.ui.graphics.drawscope.DrawScope
import entities.Pipe
import entities.Proscenium
import entities.SetPiece
import entities.SetPlatform
import entities.Shape
import entities.Wall
import org.w3c.dom.Document
import org.w3c.dom.Element

fun drawSvgContent(svgDocument: Document, svgNamespace: String, parentElement: Element) {
//  create a viewport here to surround the rest of the svg commands
//      see https://stackoverflow.com/questions/2724415/how-to-resize-an-svg-with-batik-and-display-it
  for (instance in Proscenium.instances) {
    instance.drawSvg(svgDocument, svgNamespace, parentElement)
  }
  for (instance in Wall.instances) {
    instance.drawSvg(svgDocument, svgNamespace, parentElement)
  }
  println("SVG Pipes:")
  for (instance in Pipe.instances) {
    println(instance)
    instance.drawSvg(svgDocument, svgNamespace, parentElement)
  }
  println("SVG SetPieces:")
  for (instance in SetPiece.instances) {
    println(instance)
    instance.drawSvg(svgDocument, svgNamespace, parentElement)
  }

  drawCircle(svgDocument, svgNamespace, parentElement, 349f + 36f, 1037f, 70f)
    .addAttribute("stroke", "red")
}

fun Proscenium.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element) {
//  println("Drawing the proscenium.")
  drawLine(svgDocument, svgNamespace, parentElement, x - 17f, y - 17f, x + 17f, y + 17f)
    .addAttribute("stroke", "cyan")
  drawLine(svgDocument, svgNamespace, parentElement, x + 17f, y - 17f, x - 17f, y + 17f)
    .addAttribute("stroke", "cyan")
  drawCircle(svgDocument, svgNamespace, parentElement, x, y, 17f)
    .addAttribute("stroke", "cyan")

  val startX = x - width / 2
  val startY = y
  val endX = x + width / 2
  val endY = y + depth
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
//  println("Drawing the wall from $x1,$y1 to $x2,$y2.")
  drawLine(svgDocument, svgNamespace, parentElement, start, end)
}

fun Pipe.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element) {
  drawRectangle(svgDocument, svgNamespace, parentElement, x, y, x + length, y + Pipe.Diameter)
  val offsetToCenter = length / 2
  dependents.forEach {
    val location = x + it.location + offsetToCenter
    drawLine(svgDocument, svgNamespace, parentElement, location, y - 4, location, y + 4)
  }
}

fun SetPiece.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element) {
  println("Drawing SetPiece at $origin ")
  for (platform in parts) {
    platform.drawSvg(svgDocument, svgNamespace, parentElement, origin.venue)
  }
}

fun SetPlatform.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element, placement: VenuePoint) {
  println("Drawing SetPlatform at $placement + $origin ")
  for (shape in shapes) {
    shape.drawSvg(svgDocument, svgNamespace, parentElement, placement + origin)
  }
}

fun Shape.drawSvg(svgDocument: Document, svgNamespace: String, parentElement: Element, placement: VenuePoint) {
  val originOffset = Point(placement.x - rectangle.width / 2, placement.y - rectangle.depth / 2, 0f)
  drawRectangle(svgDocument, svgNamespace, parentElement,
    originOffset.x,
    originOffset.y,
    originOffset.x + rectangle.width,
    originOffset.y + rectangle.depth)
}
