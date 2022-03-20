import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

// https://dev.to/zachklipp/remember-mutablestateof-a-cheat-sheet-10ma
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun WallButton() {
  var editing by remember { mutableStateOf(false) }
  val color = if (editing) Color.Red else Color.White
  Button(
    onClick = {
      editing = !editing
    },
    colors = ButtonDefaults.buttonColors(backgroundColor = color),
//    modifier = Modifier.
  ) {
    Text("Wall")
  }
}