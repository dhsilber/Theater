import entities.Luminaire
import entities.Pipe

class PipeManager(
  val pipe: Pipe,
  val current: Boolean = false,
) {

  companion object {
    var list: List<PipeManager> = listOf()
    var currentLuminaires: List<Luminaire> = listOf()

    init {
      buildList(0)
    }

    private fun buildList(current: Int) {
      var mutableList: MutableList<PipeManager> = mutableListOf()
      for ((index, pipe) in Pipe.instances.sortedBy { it.origin.y * -1 }.withIndex()) {
        mutableList.add(PipeManager(pipe, current == index))
      }
      list = mutableList.toList()
    }

    fun makeCurrent(entry: PipeManager): Unit {
      println("Making ${entry.pipe.id} be current")
      val index = list.indexOf(entry)
      buildList(index)
      currentLuminaires = entry.pipe.dependents.map { it.luminaire }.toList()
    }

  }
}