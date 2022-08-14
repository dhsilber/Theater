package display

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import coordinates.PagePoint

fun composeDraw(drawScope: DrawScope, orders: List<DrawingOrder>) {
  orders.map {
    when (it.operation) {
      DrawingOrderOperation.CIRCLE -> drawScope.drawCircle(
        color = it.color.compose,
        center = PagePoint.drawingOffset(it.data[0], it.data[1]),
        radius = it.data[2],
        style = Stroke(width = 2f)
      )
      DrawingOrderOperation.LINE -> drawScope.drawLine(
        color = it.color.compose,
        start = PagePoint.drawingOffset(it.data[0], it.data[1]),
        end = PagePoint.drawingOffset(it.data[2], it.data[3]),
      )
    }
  }
}
