package display

import androidx.compose.ui.graphics.Color

data class DrawingOrder(
  val operation: DrawingOrderOperation,
  val data: List<Float>,
  val color: IndependentColor = IndependentColor(Color.Black, "black"),
)
