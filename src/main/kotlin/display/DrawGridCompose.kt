package display

import Grid
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import coordinates.PagePoint
import coordinates.StagePoint
import coordinates.VenuePoint
import entities.Proscenium
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint

fun Grid.draw(drawScope: DrawScope) {
  PagePoint.Setup(drawScope.size)
  Grid.Setup(drawScope.size)

//  drawScope.drawLine(Color.Green, PagePoint.pageOffset(0f, 0f), PagePoint.pageOffset(0f, Grid.Depth - 0))
//  drawScope.drawLine(Color.Red, PagePoint.pageOffset(0f, 0f), PagePoint.pageOffset(Grid.Width, 0f))
//  drawScope.drawLine(Color.Blue,
//    PagePoint.pageOffset(0f, Grid.Depth - 0),
//    PagePoint.pageOffset(Grid.Width, Grid.Depth - 0))
//  drawScope.drawLine(Color.Cyan, PagePoint.pageOffset(Grid.Width, 0f), PagePoint.pageOffset(Grid.Width, Grid.Depth))

  verticalGridLines(drawScope)

  horizontalGridLines(drawScope)
}

private fun horizontalGridLines(drawScope: DrawScope) {
  val negativeYFromOrigin = (StagePoint.OriginY(0f) / Grid.MeasureSize).toInt()
  val yStart = StagePoint.OriginY(0f) - (negativeYFromOrigin * Grid.MeasureSize)
  val yFinish = VenuePoint.LargeY + VenuePoint.LargeY % Grid.MeasureSize
  val xBegin = 0f - PagePoint.OffsetX

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
    val direction = if (Proscenium.inUse()) -1 else 1
    drawScope.drawContext.canvas.nativeCanvas.drawString(
      (yCounter * direction).toString(),
      10f,
      yNumberPosition,

      Font(),
      Paint(),
    )
    yMarker += Grid.MeasureSize
    yCounter += 4
  }
}

private fun verticalGridLines(drawScope: DrawScope) {
  val negativeXFromOrigin = ((StagePoint.OriginX(0f) + PagePoint.OffsetX) / Grid.MeasureSize).toInt()
  val xStart =
    StagePoint.OriginX(0f) - (negativeXFromOrigin * Grid.MeasureSize) //VenuePoint.SmallX + (VenuePoint.SmallX % Grid.MeasureSize)
  val xFinish = VenuePoint.LargeX + (VenuePoint.LargeX % Grid.MeasureSize)
  val yTop = 0f

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
    xNumberPosition += if (Proscenium.inUse()) 0f else Grid.ContentSpacer
    drawScope.drawContext.canvas.nativeCanvas.drawString(
      xCounter.toString(),
      xNumberPosition,
      30f,
      Font(),
      Paint(),
    )
    xMarker += Grid.MeasureSize
    xCounter += 4
  }
}
