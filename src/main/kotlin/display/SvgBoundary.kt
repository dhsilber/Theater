package display

import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs

class SvgBoundary(
  var xMin: Float = Float.MAX_VALUE,
  var yMin: Float = Float.MAX_VALUE,
  var xMax: Float = Float.MIN_VALUE,
  var yMax: Float = Float.MIN_VALUE,
) {
  val width: Float
    get() = xMax - xMin

  val height: Float
    get() = yMax - yMin

//  fun x(x: Float) {
//    xMin = if (x < xMin) x else xMin
//    xMax = if (x > xMax) x else xMax
//  }

//  fun y(y: Float) {
//    yMin = if (y < yMin) y else yMin
//    yMax = if (y > yMax) y else yMax
//  }

  operator fun plus(svgBoundary: SvgBoundary) =
    SvgBoundary(
      min(xMin, svgBoundary.xMin),
      min(yMin, svgBoundary.yMin),
      max(xMax, svgBoundary.xMax),
      max(yMax, svgBoundary.yMax),
    )

  override fun toString() = "($xMin, $yMin, $xMax, $yMax) ($width, $height)"

  private val delta = 0.00000001

  override fun equals(other: Any?): Boolean {
    val boundary = other as SvgBoundary
    return ((xMin == boundary.xMin)
        && (xMax == boundary.xMax)
        && (yMin == boundary.yMin)
        && (yMax == boundary.yMax))
//    return ((abs(xMin - boundary.xMin) < delta)
//        && (abs(xMax - boundary.xMax) < delta)
//        && (abs(yMin - boundary.yMin) < delta)
//        && (abs(yMax - boundary.yMax) < delta))
  }
}