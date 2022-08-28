package display

import XmlElemental
import androidx.compose.ui.graphics.Color

data class DrawingOrder(
  val operation: DrawingOrderOperation,
  val entity: XmlElemental,
  val data: List<Float>,
  val useType: String = "",
  val color: IndependentColor = IndependentColor(Color.Black, "black"),
  val opacity: Float = 0F
)
