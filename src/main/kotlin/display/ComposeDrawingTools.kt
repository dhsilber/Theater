package display

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import coordinates.PagePoint
import display.DrawingOrderOperation.CIRCLE
import display.DrawingOrderOperation.LINE
import display.DrawingOrderOperation.RECTANGLE

fun composeDraw(drawScope: DrawScope, orders: List<DrawingOrder>) {
  orders.map {
    when (it.operation) {
      CIRCLE -> drawScope.drawCircle(
        color = it.color.compose,
        center = PagePoint.drawingOffset(it.data[0], it.data[1]),
        radius = it.data[2],
        style = Stroke(width = 2f)
      )
      LINE -> drawScope.drawLine(
        color = it.color.compose,
        start = PagePoint.drawingOffset(it.data[0], it.data[1]),
        end = PagePoint.drawingOffset(it.data[2], it.data[3]),
      )
      RECTANGLE -> drawScope.drawRect(
        color = it.color.compose,
        topLeft = PagePoint.drawingOffset(it.data[0], it.data[1]),
        size = PagePoint.size(it.data[2], it.data[3]),
        style = Stroke(width = 2f),
      )
    }
  }
}
