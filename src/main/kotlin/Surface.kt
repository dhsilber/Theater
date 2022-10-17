data class Surface(
  val x: Float,
  val y: Float,
  val z: Float,
  val width: Float,
  val depth: Float,
  val zDepth: Float = Float.MIN_VALUE,
) {
  fun isSloped() = Float.MIN_VALUE != zDepth
}
