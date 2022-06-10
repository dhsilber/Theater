package display

import Grid
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import coordinates.PagePoint
import coordinates.Point
import coordinates.VenuePoint
import entities.Pipe
import entities.Proscenium
import entities.SetPiece
import entities.SetPlatform
import entities.Shape
import entities.Wall

fun drawContent(drawScope: DrawScope) {
  Grid.instance.draw(drawScope)

  println("Prosceniums: ${Proscenium.instances.size}")
  for (instance in Proscenium.instances) {
    println(instance.toString())
    instance.draw(drawScope)
//          drawLine(Color.Black, PagePoint.offset(x1, y1), PagePoint.offset(x2, y2))
  }

  for (instance in Wall.instances) {
//          Text(instance.toString())
//          val (x1, y1, x2, y2) =
    instance.draw(drawScope)
//          with(density) {

//          drawLine(Color.Black, PagePoint.offset(x1, y1), PagePoint.offset(x2, y2))
//          }
//          drawLine(Color.Magenta, PagePoint.offset(x10.toFloat(), y10.toFloat()), PagePoint.offset(x20.toFloat(), y20.toFloat()))

  }

  println("Compose Pipes:")
  for (instance in Pipe.instances) {
    println(instance)
    instance.draw(drawScope)
  }

  for (instance in SetPiece.instances) {
    println(instance)
    instance.draw(drawScope)
  }
}

fun Proscenium.draw(drawScope: DrawScope) {
//  println(toString())
  drawScope.drawLine(Color.Cyan,
    PagePoint.drawingOffset(origin.x - 17, origin.y - 17),
    PagePoint.drawingOffset(origin.x + 17, origin.y + 17))
  drawScope.drawLine(Color.Cyan,
    PagePoint.drawingOffset(origin.x + 17, origin.y - 17),
    PagePoint.drawingOffset(origin.x - 17, origin.y + 17))
  drawScope.drawCircle(Color.Cyan, 17f, PagePoint.drawingOffset(origin.x, origin.y), style = Stroke(width = 2f))
  val startX = origin.x - width / 2
  val startY = origin.y
  val endX = origin.x + width / 2
  val endY = origin.y + depth
  // SR end of proscenium arch
  drawScope.drawLine(Color.Black, PagePoint.drawingOffset(startX, startY), PagePoint.drawingOffset(startX, endY))
  // SL end of proscenium arch
  drawScope.drawLine(Color.Black, PagePoint.drawingOffset(endX, startY), PagePoint.drawingOffset(endX, endY))

  // US side of proscenium arch
  drawScope.drawLine(Color.Gray, PagePoint.drawingOffset(startX, startY), PagePoint.drawingOffset(endX, startY))
  // DS side of proscenium arch
  drawScope.drawLine(Color.LightGray, PagePoint.drawingOffset(startX, endY), PagePoint.drawingOffset(endX, endY))
}

fun Wall.draw(drawScope: DrawScope) {
  println("When drawing wall, start: $start: ${start.toOffset()}, end: $end: ${end.toOffset()}")
  drawScope.drawLine(Color.Black, start.toOffset(), end.toOffset())
}

fun Pipe.draw(drawScope: DrawScope) {
  println("When drawing $this, VenuePoint extremes are: ${VenuePoint}")
  drawScope.drawRect(Color.Black,
    PagePoint.drawingOffset(origin.venue.x, origin.venue.y),
    PagePoint.size(length, Pipe.Diameter))
  val offsetToCenter = length / 2
  dependents.forEach {
    val location = origin.venue.x + it.location + offsetToCenter
    drawScope.drawLine(Color.Black,
      PagePoint.drawingOffset(location, origin.venue.y - 4),
      PagePoint.drawingOffset(location, origin.venue.y + 4))
  }
}

fun SetPiece.draw(drawScope: DrawScope) {
  println("Drawing SetPiece at $origin ")
  for (platform in parts) {
    platform.draw(drawScope, origin.venue)
  }
}

fun SetPlatform.draw(drawScope: DrawScope, placement: VenuePoint) {
  println("Drawing SetPlatform at $placement + $origin ")
  for (shape in shapes) {
    shape.draw(drawScope, placement + origin)
  }
}

fun Shape.draw(drawScope: DrawScope, placement: VenuePoint) {
  val originOffset = Point(0 - rectangle.width / 2, 0 - rectangle.depth / 2, 0f)
  drawScope.drawRect(
    color = Color.Black,
    topLeft = (placement + originOffset).toOffset(),
    size = PagePoint.size(rectangle.width, rectangle.depth),
    style = Stroke(width = 2f),
  )
  println("Drawing shape at $placement with ${rectangle.width}, ${rectangle.depth}")
//  for(platform in parts) {
//    platform.draw(origin)
//  }
}













