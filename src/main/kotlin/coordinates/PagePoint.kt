package coordinates

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import java.lang.Float.min

class PagePoint {
  companion object {
    var OffsetX = 0f
    var OffsetY = 0f
    var Scale = 1f
    var shiftY = 5f


    fun scale(scale: Float) {
      Scale = scale
    }

    fun Setup(size: Size) {
      val widthRatio = size.width / (VenuePoint.VenueWidth + Grid.ContentSpacer * 2)
      val depthRatio = size.height / (VenuePoint.VenueDepth + Grid.ContentSpacer * 2)
      scale(min(widthRatio, depthRatio) * 0.97f)

      OffsetX = 0f - VenuePoint.SmallX
      OffsetY = 0f - VenuePoint.SmallY
    }

    fun drawingOffset(x: Float, y: Float): Offset {
      return Offset(
        (x + OffsetX) * Scale + Grid.ContentSpacer,
        (y + shiftY + OffsetY) * Scale + Grid.ContentSpacer
      )
    }

    fun pageOffset(x: Float, y: Float): Offset {
      return Offset(
        (x ) * Scale, //+ SpacerG,
        (y + shiftY ) * Scale //+ SpacerG
      )
    }

    fun size(width: Float, depth: Float): Size {
      return Size(
        width * Scale,
        depth * Scale
      )
    }

    fun distance(length: Float): Float {
      return length * Scale
    }

  }
}
