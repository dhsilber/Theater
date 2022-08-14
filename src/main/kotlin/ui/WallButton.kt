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
fun WallButton(drawingWalls: Boolean, onDrawingChange : () -> Unit) {
  val color = if (drawingWalls) Color.Red else Color.White
  Button(
    onClick = onDrawingChange,
    colors = ButtonDefaults.buttonColors(backgroundColor = color),
//    modifier = Modifier.
  ) {
    Text("Wall")
  }
}
