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

fun drawPlanContent(drawScope: DrawScope, showSet: Boolean) {
  Grid.instance.drawPlan(drawScope)
  println(Grid.toString())
  println(Venue.toString())
  println(VenuePoint.toString())

  for (instance in Wall.instances) {
//          Text(instance.toString())
//          val (x1, y1, x2, y2) =
    instance.draw(drawScope)
//          with(density) {

//          drawLine(Color.Black, PagePoint.offset(x1, y1), PagePoint.offset(x2, y2))
//          }
//          drawLine(Color.Magenta, PagePoint.offset(x10.toFloat(), y10.toFloat()), PagePoint.offset(x20.toFloat(), y20.toFloat()))

  }

  Venue.instances.map { composeDraw(drawScope, it.drawPlan()) }
  Floor.instances.map { composeDraw(drawScope, it.drawPlan()) }
  Stair.instances.map { composeDraw(drawScope, it.drawPlan()) }
  Proscenium.instances.map { composeDraw(drawScope, it.drawPlan()) }
  PipeBase.instances.map { composeDraw(drawScope, it.drawPlan()) }
  PipeManager.list.map { composeDraw(drawScope, it.pipe.drawPlan()) }

  if (showSet) {
    for (instance in Setpiece.instances) {
      instance.draw(drawScope)
    }
    Flat.instances.map { composeDraw(drawScope, it.drawPlan()) }
  }
}

fun drawSectionContent(drawScope: DrawScope, showSet: Boolean) {
//  Grid.instance.drawSection(drawScope)

//  for (instance in Wall.instances) {
//    instance.draw(drawScope)
//  }

  Venue.instances.map { composeDraw(drawScope, it.drawSection()) }
  Floor.instances.map { composeDraw(drawScope, it.drawSection()) }
  Stair.instances.map { composeDraw(drawScope, it.drawSection()) }
  Proscenium.instances.map { composeDraw(drawScope, it.drawSection()) }
  PipeBase.instances.map { composeDraw(drawScope, it.drawSection()) }
  PipeManager.list.map { composeDraw(drawScope, it.pipe.drawSection()) }

//  for (instance in Setpiece.instances) {
//    instance.draw(drawScope)
//  }
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

fun Wall.draw(drawScope: DrawScope) {
  drawScope.drawLine(Color.Black, start.toOffset(), end.toOffset())
}

