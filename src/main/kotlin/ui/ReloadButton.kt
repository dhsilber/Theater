package ui

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import reload

@Composable
fun ReloadButton() {
  Button(
    onClick = { reload() },
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.Cyan),
  ) {
    Text("Reload")
  }
}
