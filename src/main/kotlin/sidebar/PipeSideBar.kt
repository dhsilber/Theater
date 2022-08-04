package sidebar

import PipeManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import entities.Luminaire

class PipeSideBar {

  companion object {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun pipeLister(pipes: List<PipeManager>, selectPipe: (PipeManager) -> Unit) {

      Column(
        modifier = Modifier
          .padding(20.dp)
          .border(BorderStroke(2.dp, Color.Magenta))
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
      ) {
        Text("Pipe Sidebar")
        for (pipeOrdering in pipes) {
          Box(
            modifier = Modifier
              .fillMaxWidth(1f)
              .combinedClickable(
                onClick = { selectPipe(pipeOrdering) },
              ),

            ) {
            Column(
              modifier = Modifier.padding(20.dp)
            ) {
              val pipe = pipeOrdering.pipe
              Text("${pipeOrdering.current} -- Name: ${pipe.id} at ${pipe.origin}")
              luminaireLister(pipe.dependents
                .filter { it.hangable is Luminaire }
                .map { it.hangable as Luminaire }
                .toList())
            }
          }
        }
      }
    }
  }
}

@Composable
fun luminaireLister(luminaires: List<Luminaire>) {
  for (item in luminaires) {
    Row() {
//      println("--- ${item.type} --- ${item.location} ---")
      Text("--- ${item.type} --- ${item.location} ---")
    }
  }
}