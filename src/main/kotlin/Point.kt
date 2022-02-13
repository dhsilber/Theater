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
  val x: Double,
  val y: Double,
  val z: Double,
) {
  constructor(x: Int, y: Int, z: Int) : this(x.toDouble(),y.toDouble(),z.toDouble())
}