package coordinates

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
        proscenium.origin.x + x,
        proscenium.origin.y - y,
        proscenium.origin.z + z
      )
    } else {
      VenuePoint(x, y, z)
    }

//  override fun toString() : String{
//    return "coordinates.StagePoint: $x, $y, $z"
//  }

  companion object {
    fun OriginX(x: Float): Float {
      return if (Proscenium.inUse()) {
        val proscenium = Proscenium.get()
        proscenium.origin.x + x
      } else {
        x
      }
    }

    fun OriginY(y: Float): Float {
      return if (Proscenium.inUse()) {
        val proscenium = Proscenium.get()
        proscenium.origin.y - y
      } else {
        y
      }
    }
  }
}