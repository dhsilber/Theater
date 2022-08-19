package display

import Grid
import PipeManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import coordinates.PagePoint
import coordinates.Point
import coordinates.VenuePoint
import entities.*

fun drawContent(drawScope: DrawScope) {
  Grid.instance.draw(drawScope)

  for (instance in Wall.instances) {
//          Text(instance.toString())
//          val (x1, y1, x2, y2) =
    instance.draw(drawScope)
//          with(density) {

//          drawLine(Color.Black, PagePoint.offset(x1, y1), PagePoint.offset(x2, y2))
//          }
//          drawLine(Color.Magenta, PagePoint.offset(x10.toFloat(), y10.toFloat()), PagePoint.offset(x20.toFloat(), y20.toFloat()))

  }

  Stair.instances.map { composeDraw(drawScope, it.drawPlan()) }
  composeDraw(drawScope, Proscenium.instances.first().drawPlan())

  for (instance in PipeBase.instances) {
    instance.draw(drawScope)
  }

  for (item in PipeManager.list) {
    val instance = item.pipe
    instance.draw(drawScope, item.current)
  }

  for (instance in Setpiece.instances) {
    instance.draw(drawScope)
  }
}

fun Wall.draw(drawScope: DrawScope) {
  drawScope.drawLine(Color.Black, start.toOffset(), end.toOffset())
}

fun PipeBase.draw(drawScope: DrawScope) {
  drawScope.drawCircle(
    color = Color.Black,
    radius = 18f,
    center = PagePoint.drawingOffset(origin.venue.x, origin.venue.y),
    style = Stroke(width = 2f)
  )
}

fun Pipe.draw(drawScope: DrawScope, highlight: Boolean) {
  when (vertical) {
    true -> drawVertical(drawScope)
    else -> drawHorizontal(drawScope, highlight)
  }
}

fun Pipe.drawVertical(drawScope: DrawScope) {
//  val color = if (highlight) Color.Magenta else Color.Black
  drawScope.drawCircle(
    color = Color.Black,
    radius = 2f,
    center = PagePoint.drawingOffset(origin.venue.x, origin.venue.y),
    style = Stroke(width = 2f)
  )
//  val offsetToCenter = length / 2
//  dependents.forEach {
//    val location = origin.venue.x + it.location + offsetToCenter
//    drawScope.drawLine(Color.Black,
//      PagePoint.drawingOffset(location, origin.venue.y - 4),
//      PagePoint.drawingOffset(location, origin.venue.y + 4))
//  }
}

fun Pipe.drawHorizontal(drawScope: DrawScope, highlight: Boolean) {
  val color = if (highlight) Color.Magenta else Color.Black
  drawScope.drawRect(
    color = color,
    topLeft = PagePoint.drawingOffset(origin.venue.x, origin.venue.y),
    size = PagePoint.size(length, Pipe.Diameter)
  )
  val offsetToCenter = length / 2
  dependents.forEach {
    val location = origin.venue.x + it.location + offsetToCenter
    drawScope.drawLine(Color.Black,
      PagePoint.drawingOffset(location, origin.venue.y - 4),
      PagePoint.drawingOffset(location, origin.venue.y + 4))
  }
}

fun Setpiece.draw(drawScope: DrawScope) {
  for (platform in parts) {
    platform.draw(drawScope, origin.venue)
  }
}

fun SetPlatform.draw(drawScope: DrawScope, placement: VenuePoint) {
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
//  for(platform in parts) {
//    platform.draw(origin)
//  }
}
