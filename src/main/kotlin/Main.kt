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
import entities.Venue
import sidebar.PipeSideBar

//import org.jetbrains.compose.splitpane.demo.uiTop

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {

  var text by remember { mutableStateOf("Hello, World!") }
  var x by remember { mutableStateOf(0) }
  var y by remember { mutableStateOf(0) }


  MaterialTheme {

    var drawingWalls by remember { mutableStateOf(false) }
    var pipeDisplay by remember { mutableStateOf(true) }

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
        PipeButton(pipeDisplay = pipeDisplay, togglePipeDisplay = { pipeDisplay = !pipeDisplay })
        Button(onClick = { Svg.writePlan() }) {
          Text("Write SVG")
        }
      }
      Display.display(
        drawingWalls,
        share = pipeDisplay,
        x, y, text,
      )
      if (pipeDisplay) {
        PipeSideBar.pipeLister(PipeManager.list, PipeManager::makeCurrent)
      }

    }
  }
}


fun main() = application {
  Configuration
  Startup().startup("${Configuration.plotDirectory}/${Configuration.plotFilename}")
  val title = try {
    Venue.instances[0].building + " - " + Venue.instances[0].room
  } catch (exception: Exception) {
    "Missing venue"
  }

  PipeManager.Companion
  Svg.writeAll()
  Html.writeAll()
  exitApplication()

  val state = rememberWindowState(placement = WindowPlacement.Maximized)

  Window(onCloseRequest = ::exitApplication, title = title, state = state) {
    App()
  }
}
