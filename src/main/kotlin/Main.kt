// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mobiletheatertech.plot.Configuration
import com.mobiletheatertech.plot.Startup
import com.mobiletheatertech.plot.Svg
import entities.Venue

//import org.jetbrains.compose.splitpane.demo.uiTop

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {

  var text by remember { mutableStateOf("Hello, World!") }
  var x by remember { mutableStateOf(0) }
  var y by remember { mutableStateOf(0) }

  fun setter(foo: String) {
    text = foo
  }

  MaterialTheme {

    var drawingWalls by remember { mutableStateOf(false) }

    Row(
      Modifier
        .fillMaxSize()
        .onPointerEvent(PointerEventType.Press) {
          text = it.awtEvent.locationOnScreen?.toString().orEmpty()
          x = it.awtEvent.locationOnScreen.x
          y = it.awtEvent.locationOnScreen.y
        },
      Arrangement.spacedBy(5.dp)
    ) {
      Column(
        Modifier.padding(8.dp)
      )
      {
        WallButton(drawingWalls = drawingWalls, onDrawingChange = { drawingWalls = !drawingWalls })
        Button(onClick = { Svg.write() }) {
          Text("Write SVG")
        }
      }
      Display.display(
        drawingWalls,
        x, y, text,
//        ::setter
      )

    }
  }
//
//    var text by remember { mutableStateOf("Hello, World!") }
//
//  MaterialTheme {
//    Button(onClick = {
//      text = "Hello, Desktop!"
//    }) {
//      Text(text)
//    }
}


fun main() = application {
  Configuration
  Startup().startup("${Configuration.plotDirectory}/${Configuration.plotFilename}")
  val title = try {
    Venue.Instances[0].building + " - " + Venue.Instances[0].room
  }
  catch(exception: Exception) {
    "Missing venue"
  }
  val state = rememberWindowState(placement = WindowPlacement.Maximized)

  Window(onCloseRequest = ::exitApplication, title = title, state = state) {
    App()
  }
}
