package ui

//import androidx.compose.foundation.layout.fillMaxWidth

// https://dev.to/zachklipp/remember-mutablestateof-a-cheat-sheet-10ma
//import androidx.compose.ui.Modifier
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PipeButton(pipeDisplay: Boolean, togglePipeDisplay : () -> Unit) {
  val color = if (pipeDisplay) Color.Magenta else Color.White
  Button(
    onClick = togglePipeDisplay,
    colors = ButtonDefaults.buttonColors(backgroundColor = color),
  ) {
    Text("Pipe")
  }
}
