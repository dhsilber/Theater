package ui

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ToggleButton(active: Boolean, prompt: String, color: Color, activeColor: Color, toggle: () -> Unit) {
    val backgroundColor = if(active) activeColor else color
    Button(
    onClick = toggle,
    colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor, contentColor = Color.Black),
    ) {
        Text(prompt)
    }
}
