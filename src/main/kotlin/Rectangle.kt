data class Rectangle(
  val width: Float,
  val depth: Float,
  ) {
  companion object {
    val Empty = Rectangle( 0f, 0f)
  }
}