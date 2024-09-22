import entities.Pipe

class PipeManager(
  val pipe: Pipe,
  val current: Boolean = false,
) {

  companion object {
    var current = 0
    var list: List<PipeManager> = listOf()
    var currentLuminaires: List<Hangable> = listOf()
    var display: Boolean = true

    fun buildList(current: Int) {
      var mutableList: MutableList<PipeManager> = mutableListOf()
      for ((index, pipe) in Pipe.instances.sortedBy { it.origin.y * -1 }.withIndex()) {
        mutableList.add(PipeManager(pipe, current == index))
      }
      list = mutableList.toList()
    }

    fun makeCurrent(entry: PipeManager) {
      println("Making ${entry.pipe.id} be current")
      val index = list.indexOf(entry)
      current = index
      buildList(index)
      currentLuminaires = entry.pipe.dependents.map { it.hangable }.toList()
    }

    fun clear() {
      list = listOf()
      currentLuminaires = listOf()
    }

  }
}
