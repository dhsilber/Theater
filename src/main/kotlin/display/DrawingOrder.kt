package display

import XmlElemental
import androidx.compose.ui.graphics.Color

data class DrawingOrder(
  val operation: DrawingOrderOperation,
  val entity: XmlElemental? = null, // Nullable only to support interactive creation of entitiesq
  val data: List<Float>,
  val useType: String = "",
  val color: IndependentColor = IndependentColor(Color.Black, "black"),
  val opacity: Float = 0F,
  val explanation: String = "",
  val useOrientation: Float = 0F,
)
