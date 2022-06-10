package display

import Grid
import coordinates.Point
import coordinates.VenuePoint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import coordinates.PagePoint
import coordinates.StagePoint
import entities.Pipe
import entities.Wall
import entities.Proscenium
import entities.SetPiece
import entities.SetPlatform
import entities.Shape
import org.jetbrains.skia.Paint

fun drawContent(drawScope: DrawScope) {
  Grid.instance.draw(drawScope)

  println("Prosceniums: ${Proscenium.instances.size}")
  for (instance in Proscenium.instances) {
    println(instance.toString())
//          val (x1, y1, x2, y2) =
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

fun Grid.draw(drawScope: DrawScope) {
  PagePoint.Setup(drawScope.size)
  Grid.Setup(drawScope.size)
  println("When drawing grid (${Grid}), VenuePoint extremes are: ${VenuePoint}")

  drawScope.drawLine(Color.Green, PagePoint.pageOffset(0f, 0f), PagePoint.pageOffset(0f, Grid.Depth - 0))
  drawScope.drawLine(Color.Red, PagePoint.pageOffset(0f, 0f), PagePoint.pageOffset(Grid.Width, 0f))
  drawScope.drawLine(Color.Blue,
    PagePoint.pageOffset(0f, Grid.Depth - 0),
    PagePoint.pageOffset(Grid.Width, Grid.Depth - 0))
  drawScope.drawLine(Color.Cyan, PagePoint.pageOffset(Grid.Width, 0f), PagePoint.pageOffset(Grid.Width, Grid.Depth))

  val negativeXFromOrigin = ((StagePoint.OriginX(0f) + PagePoint.OffsetX)/ Grid.MeasureSize).toInt()
  val xStart =
    StagePoint.OriginX(0f) - (negativeXFromOrigin * Grid.MeasureSize) //VenuePoint.SmallX + (VenuePoint.SmallX % Grid.MeasureSize)
  val xFinish = VenuePoint.LargeX + (VenuePoint.LargeX % Grid.MeasureSize)
  val yTop = 0f
//  val yBottom = yTop + (VenuePoint.LargeY - VenuePoint.SmallY)

  var xCounter = 0 - negativeXFromOrigin * 4
  var xMarker = xStart
  while (xFinish >= xMarker) {
    drawScope.drawLine(
      Color.Gray,
      PagePoint.drawingOffset(xMarker, yTop),
      PagePoint.drawingOffset(xMarker, Grid.Depth),
      alpha = 0.2f,
    )
    var xNumberPosition = xMarker * PagePoint.Scale + PagePoint.OffsetX + Grid.ContentSpacer
    xNumberPosition += if(Proscenium.inUse()) 0f else Grid.ContentSpacer
    drawScope.drawContext.canvas.nativeCanvas.drawString(
      xCounter.toString(),
//      xMarker.dp.value * PagePoint.Scale,
      xNumberPosition,
      30f,
      org.jetbrains.skia.Font(),
      Paint(),
    )
    xMarker += Grid.MeasureSize
    xCounter += 4
  }

  val negativeYFromOrigin = (StagePoint.OriginY(0f) / Grid.MeasureSize).toInt()
  val yStart = StagePoint.OriginY(0f) - (negativeYFromOrigin * Grid.MeasureSize)
  //VenuePoint.SmallY + VenuePoint.SmallY % Grid.MeasureSize
  val yFinish = VenuePoint.LargeY + VenuePoint.LargeY % Grid.MeasureSize
  val xBegin = 0f - PagePoint.OffsetX
//  val xEnd = xBegin + (VenuePoint.LargeX - VenuePoint.SmallY)

  var yCounter = 0 - negativeYFromOrigin * 4
  var yMarker = yStart
  while (yFinish >= yMarker) {
    drawScope.drawLine(
      Color.Gray,
      PagePoint.drawingOffset(xBegin, yMarker),
      PagePoint.drawingOffset(Grid.Width - PagePoint.OffsetX, yMarker),
      alpha = 0.3f,
    )
    val yNumberPosition = yMarker * PagePoint.Scale + PagePoint.OffsetY + Grid.ContentSpacer
    val direction = if(Proscenium.inUse()) -1 else 1
    drawScope.drawContext.canvas.nativeCanvas.drawString(
      (yCounter * direction).toString(),
      10f,
//      yMarker.dp.value * PagePoint.Scale,
      yNumberPosition,

      org.jetbrains.skia.Font(),
      Paint(),
    )
    yMarker += Grid.MeasureSize
    yCounter += 4
  }


//  StagePoint.OriginX(0f)
//
//
//  var x = PagePoint.OffsetX
//  while (x > 0f) {
//    x -= Grid.MeasureSize * 2
//  }
//  while (x < Grid.Width) {
//    drawScope.drawRect(Color.Cyan,
//      PagePoint.pageOffset(x, 4f),
//      PagePoint.size(Grid.MeasureSize, Grid.ScaleSpacer))
//
//    x += Grid.MeasureSize * 2
//  }
//
//  drawScope.drawContext.canvas.nativeCanvas.drawString("123", 30f, 30f, org.jetbrains.skia.Font(), Paint())
//
//  drawScope.drawRect(Color.Cyan,
//    PagePoint.pageOffset(Grid.ScaleSpacer, Grid.ContentSpacer),
//    PagePoint.size(Grid.ScaleSpacer, Grid.MeasureSize))
}

//private infix fun <A, B> Pair<A, B>.step(measureSize: B): Any {
//
//}

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













