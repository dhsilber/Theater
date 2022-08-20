package coordinates

/**
 * Represent a point in the plot space.
 *
 *
 * Keeps track of extremes of ranges of coordinate values across all instances.
 *
 * @author dhs
 * @since 0.0.2
 */
data class VenuePoint(
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

  fun toOffset() = PagePoint.drawingOffset(x, y)

//  operator fun plus(pointOffset: PointOffset): VenuePoint {
//    return VenuePoint(x + pointOffset.x, y + pointOffset.y, z + pointOffset.z)
//  }

  operator fun plus(point: Point) =
    VenuePoint(x + point.x, y + point.y, z + point.z)

  operator fun plus(point: StagePoint) =
    VenuePoint(x + point.x, y - point.y, z + point.z)

  companion object {
    var SmallX = Float.MAX_VALUE
    var SmallY = Float.MAX_VALUE
    var SmallZ = Float.MAX_VALUE

    var LargeX = Float.MIN_VALUE
    var LargeY = Float.MIN_VALUE
    var LargeZ = Float.MIN_VALUE

    val VenueWidth
      get() = LargeX - SmallX

    val VenueDepth
      get() = LargeY - SmallY

    fun clear() {
      SmallX = Float.MAX_VALUE
      SmallY = Float.MAX_VALUE
      SmallZ = Float.MAX_VALUE

      LargeX = Float.MIN_VALUE
      LargeY = Float.MIN_VALUE
      LargeZ = Float.MIN_VALUE
    }

    override fun toString() =
      "Small: ($SmallX, $SmallY, $SmallZ)  Large: ($LargeX, $LargeY, $LargeZ)"
  }
}