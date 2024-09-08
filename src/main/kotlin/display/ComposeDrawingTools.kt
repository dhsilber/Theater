package display

import PipeManager
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import coordinates.PagePoint
import display.DrawingOrderOperation.CIRCLE
import display.DrawingOrderOperation.LINE
import display.DrawingOrderOperation.RECTANGLE
import display.DrawingOrderOperation.FILLED_RECTANGLE
import display.DrawingOrderOperation.FILLED_RIGHT_TRIANGLE
import display.DrawingOrderOperation.USE
import entities.LuminaireDefinition
import entities.Pipe

fun composeDraw(drawScope: DrawScope, orders: List<DrawingOrder>) {
  orders.map {
    val color = highlightIfCurrentPipe(it)

    when (it.operation) {
      CIRCLE -> drawCircle(drawScope, it)
      LINE -> drawLine(drawScope, it)
      RECTANGLE -> drawRectangle(drawScope, it, color)
      FILLED_RECTANGLE -> drawFilledRectangle(drawScope, it, color)
      USE -> drawUse(drawScope, it)
      FILLED_RIGHT_TRIANGLE -> {}
    }
  }
}

private fun highlightIfCurrentPipe(drawingOrder: DrawingOrder) =
  if (drawingOrder.entity is Pipe
    && PipeManager.display
    && PipeManager.list[PipeManager.current].pipe == drawingOrder.entity
  ) {
    Color.Magenta
  } else {
    drawingOrder.color.compose
  }

private fun drawCircle(
  drawScope: DrawScope,
  drawingOrder: DrawingOrder,
) =
  drawScope.drawCircle(
    color = drawingOrder.color.compose,
    center = PagePoint.drawingOffset(drawingOrder.data[0], drawingOrder.data[1]),
    radius = drawingOrder.data[2] * PagePoint.Scale,
    style = Stroke(width = 2f)
  )

private fun drawLine(drawScope: DrawScope, drawingOrder: DrawingOrder) =
  drawScope.drawLine(
    color = drawingOrder.color.compose,
    start = PagePoint.drawingOffset(drawingOrder.data[0], drawingOrder.data[1]),
    end = PagePoint.drawingOffset(drawingOrder.data[2], drawingOrder.data[3]),
  )

//private fun drawRectangle(
//  drawScope: DrawScope,
//  drawingOrder: DrawingOrder,
//  color: Color,
//) =
//  drawScope.drawRect(
//    color = color,
//    topLeft = PagePoint.drawingOffset(drawingOrder.data[0], drawingOrder.data[1]),
//    size = PagePoint.size(drawingOrder.data[2], drawingOrder.data[3]),
//    style = Stroke(width = 2f),
//  )

private fun drawRectangle(
  drawScope: DrawScope,
  drawingOrder: DrawingOrder,
  color: Color,
) {
  val x = drawingOrder.data[0]
  val y = drawingOrder.data[1]
  val width = drawingOrder.data[2]
  val depth = drawingOrder.data[3]
  val rotation = if( drawingOrder.data.size > 4) drawingOrder.data[4] else 0f
  val center = PagePoint.drawingOffset(x + width / 2, y + depth / 2)
  drawScope.rotate(degrees = rotation, pivot = center) {
    drawRect(
      color = color,
      topLeft = PagePoint.drawingOffset(x, y),
      size = PagePoint.size(width, depth),
      style = Stroke(width = 2f),
    )
  }
}

private fun drawFilledRectangle(
  drawScope: DrawScope,
  drawingOrder: DrawingOrder,
  color: Color,
) =
  drawScope.drawRect(
    color = color.copy(alpha = drawingOrder.opacity),
    topLeft = PagePoint.drawingOffset(drawingOrder.data[0], drawingOrder.data[1]),
    size = PagePoint.size(drawingOrder.data[2], drawingOrder.data[3]),
  )

private fun drawUse(drawScope: DrawScope, drawingOrder: DrawingOrder) {
  val type = drawingOrder.useType
  val x = drawingOrder.data[0]
  val y = drawingOrder.data[1]
  val luminaireDefinition = LuminaireDefinition.findByName(type)
  val size = (luminaireDefinition?.length?.coerceAtLeast(luminaireDefinition.width) ?: 0f) / 2
  drawScope.drawLine(
    color = Color.Black,
    start = PagePoint.drawingOffset(x, y - size),
    end = PagePoint.drawingOffset(x, y + size)
  )
}


