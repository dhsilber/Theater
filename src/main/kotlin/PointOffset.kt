import entities.Proscenium

data class PointOffset(
  val x: Float,
  val initialY: Float,
  val z: Float = 0f,
) {
  val y: Float
    get() = if (Proscenium.inUse()) {
      0 - initialY
    } else
      initialY

  companion object {
    val ZERO = PointOffset(0f, 0f, 0f)
  }
}