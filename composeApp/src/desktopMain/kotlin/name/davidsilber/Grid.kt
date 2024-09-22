import androidx.compose.ui.geometry.Size
import coordinates.PagePoint
import coordinates.VenuePoint

class Grid {
  companion object {
    val instance = Grid()
    // TODO The constant spacers fail to make sense when the drawing is very small.
    // OTOH, the very tiny drawing is kind of useless.
    const val ContentSpacer = 20f
    const val ScaleSpacer = 10f
    const val MeasureSize = 48f

    val Width
      get() = VenuePoint.VenueWidth + ContentSpacer * 2

    val Depth
      get() = VenuePoint.VenueDepth + ContentSpacer * 2

    fun Setup(size: Size) {
//      val foo = 0f//20f
//      val widthRatio = size.width / (VenueWidth+foo)
//      val depthRatio = size.height / (VenueDepth+foo)
//      PagePoint.scale(min(widthRatio, depthRatio) * 0.97f)
//      PagePoint.Setup(size)
    }

    override fun toString(): String {
      return "Width: $Width  Depth: $Depth"
    }
  }
}

