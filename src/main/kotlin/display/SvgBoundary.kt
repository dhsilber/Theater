package display

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

  fun x(x: Float) {
    xMin = if (x < xMin) x else xMin
    xMax = if (x > xMax) x else xMax
  }

  fun y(y: Float) {
    yMin = if (y < yMin) y else yMin
    yMax = if (y > yMax) y else yMax
  }

  override fun toString(): String {
    return "($xMin, $yMin, $xMax, $yMax) ($width, $height)"
  }

  override fun equals(other: Any?): Boolean {
    val boundary = other as SvgBoundary
    return ((xMin == boundary.xMin)
        && (xMax == boundary.xMax)
        && (yMin == boundary.yMin)
        && (yMax == boundary.yMax))
  }
}