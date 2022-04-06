import androidx.compose.ui.geometry.Offset

/**
 * Represent a point in the plot space.
 *
 *
 * Keeps track of extremes of ranges of coordinate values across all instances.
 *
 * @author dhs
 * @since 0.0.2
 */
data class Point(
  val x: Float,
  val y: Float,
  val z: Float,
) {
  constructor(x: Int, y: Int, z: Int) : this(x.toFloat(), y.toFloat(), z.toFloat())

  init {
    SmallX = if (x < SmallX) x else SmallX
    SmallY = if (y < SmallY) y else SmallY
    SmallZ = if (z < SmallZ) z else SmallZ

    LargeX = if (x > LargeX) x else LargeX
    LargeY = if (y > LargeY) y else LargeY
    LargeZ = if (z > LargeZ) z else LargeZ
  }

  fun toOffset() = Offset(x, y)

  companion object {
    var SmallX = Float.MAX_VALUE
    var SmallY = Float.MAX_VALUE
    var SmallZ = Float.MAX_VALUE

    var LargeX = Float.MIN_VALUE
    var LargeY = Float.MIN_VALUE
    var LargeZ = Float.MIN_VALUE

    override fun toString(): String {
      return "Small: ($SmallX, $SmallY, $SmallZ)  Large: ($LargeX, $LargeY, $LargeZ)"
    }
  }
}