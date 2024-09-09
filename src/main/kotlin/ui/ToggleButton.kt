package ui

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class DisplayState(
    val backgroundColor: Color,
    val textColor: Color,
    val prompt: String,
)

@Composable
fun ToggleButton(active: Boolean, toggle: () -> Unit, displayStateOff: DisplayState, displayStateOn: DisplayState) {
    val backgroundColor = if(active) displayStateOn.backgroundColor else displayStateOff.backgroundColor
    val textColor = if(active) displayStateOn.textColor else displayStateOff.textColor
    val prompt = if(active) displayStateOn.prompt else displayStateOff.prompt
    Button(
    onClick = toggle,
    colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor, contentColor = textColor),
    ) {
        Text(prompt)
    }
}
