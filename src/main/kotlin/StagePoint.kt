import entities.Proscenium

data class StagePoint(
  val x: Float,
  val y: Float,
  val z: Float,
) {
  val initial = Point(x, y, z)
  val venue =
    if (Proscenium.inUse()) {
      val proscenium = Proscenium.get()
      VenuePoint(
        proscenium.x + x,
        proscenium.y - y,
        proscenium.z + z
      )
    } else {
      VenuePoint(x, y, z)
    }

//  override fun toString() : String{
//    return "StagePoint: $x, $y, $z"
//  }
}