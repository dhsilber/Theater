import androidx.compose.material.Text
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.awt.awtEvent
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.mobiletheatertech.plot.Wall

class Display {

  companion object {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun display(
      x: Int, y: Int, text: String
//      resetter: (text: String) -> Unit
    ) {
      var x10 by remember { mutableStateOf(0) }
      var y10 by remember { mutableStateOf(0) }
      var x20 by remember { mutableStateOf(0) }
      var y20 by remember { mutableStateOf(0) }
      val density = LocalDensity.current
      var width by remember { mutableStateOf(0.dp) }
      var height by remember { mutableStateOf(0.dp) }

      Canvas(
        Modifier
          .fillMaxWidth(1.0f)
          .fillMaxHeight(1.0f)
//          .onPointerEvent(PointerEventType.Press) {
//            x10 = x20
//            y10 = y20
//            x20 = it.awtEvent.locationOnScreen.x
//            y20 = it.awtEvent.locationOnScreen.y
//            println("Event ${it.awtEvent}")
////            resetter(it.awtEvent.locationOnScreen.toString())
//          }
          .clipToBounds()
          .onSizeChanged {
            with(density) {
              width = it.width.toDp()
              height = it.height.toDp()
            }
          }
      ) {
        if (x != x20) {
          x10 = x20
          y10 = y20
          x20 = x
          y20 = y
//          println( "($x, $y)")
//          println( "($x10, $y10) to ($x20, $y20)")
          Wall.factorial(x10.toFloat(), y10.toFloat(), x20.toFloat(), y20.toFloat())
        }
        for (instance in Wall.Instances) {
//          Text(instance.toString())
          val (x1, y1, x2, y2) = instance.draw()
//          with(density) {

          drawLine(Color.Black, Offset(x1, y1), Offset(x2, y2))
//          }
//          drawLine(Color.Magenta, Offset(x10.toFloat(), y10.toFloat()), Offset(x20.toFloat(), y20.toFloat()))

        }

      }
      println(width.toString() + "," + height.toString())

//      for (instance in Wall.Instances) {
//        Text(instance.toString())
//        instance.draw()
//      }
//        instance.svg()

    }
  }
}

//private operator fun Int.getValue(nothing: Nothing?, property: KProperty<Int>): Int {
//
//}

data class Line(
  val x1: Float,
  val y1: Float,
  val x2: Float,
  val y2: Float,
)