import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import entities.Wall
import display.drawContent

class Display {

  companion object {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun display(
      drawingWalls: Boolean,
      share: Boolean,
      x: Int,
      y: Int,
//      text: String
//      resetter: (text: String) -> Unit
    ) {
      var x10 by remember { mutableStateOf(0) }
      var y10 by remember { mutableStateOf(0) }
      var x20 by remember { mutableStateOf(0) }
      var y20 by remember { mutableStateOf(0) }
      val density = LocalDensity.current
      var width by remember { mutableStateOf(0f) }
      var height by remember { mutableStateOf(0f) }

      var fraction = if (share) 0.5f else 1f

      Canvas(
        Modifier
          .fillMaxWidth(fraction)
          .fillMaxHeight(1.0f)
//          .onPointerEvent(PointerEventType.Press) {
//            x10 = x20
//            y10 = y20
//            x20 = it.awtEvent.locationOnScreen.x
//            y20 = it.awtEvent.locationOnScreen.y
//            println("Event ${it.awtEvent}")
////            resetter(it.awtEvent.locationOnScreen.toString())
//          }
//          .clipToBounds()
          .onSizeChanged {
            with(density) {
              width = it.width.toFloat()
              height = it.height.toFloat()
            }

          }
      ) {
        width = size.width
        height = size.height

        if (drawingWalls && x != x20) {
          x10 = x20
          y10 = y20
          x20 = x
          y20 = y
//          println( "($x, $y)")
//          println( "($x10, $y10) to ($x20, $y20)")
          Wall.createNew(x10.toFloat(), y10.toFloat(), x20.toFloat(), y20.toFloat())
        }

        drawContent(this)

      }
//      Text("Width: $width - height: $height")
//      println("Width: $width - height: $height")

//      for (instance in Wall.Instances) {
//        Text(instance.toString())
//        instance.draw()
//      }
//        instance.svg()

    }
  }
}
