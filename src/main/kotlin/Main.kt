// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mobiletheatertech.plot.Configuration
import entities.Venue
import sidebar.MessageSideBar
import sidebar.PipeSideBar
import ui.DisplayState
import ui.PipeButton
import ui.ReloadButton
import ui.ToggleButton as ViewButton
import ui.ToggleButton as MessageButton
import ui.ToggleButton as FlareVenueButton
import ui.WallButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {

    var text by remember { mutableStateOf("Hello, World!") }
    var x by remember { mutableStateOf(0) }
    var y by remember { mutableStateOf(0) }


    MaterialTheme {

        var drawingWalls by remember { mutableStateOf(false) }
        var pipeDisplay by remember { mutableStateOf(false) }
        var messagesDisplay by remember { mutableStateOf(true) }
        var viewSection by remember { mutableStateOf(false) }
        var flareVenueCorners by remember { mutableStateOf(true) }

        Row(
            Modifier
                .fillMaxSize()
                .onPointerEvent(PointerEventType.Press) {
                    text = it.awtEvent.locationOnScreen.toString()
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
                PipeButton(pipeDisplay = pipeDisplay, togglePipeDisplay = {
                    pipeDisplay = !pipeDisplay
                    PipeManager.display = pipeDisplay
                })
                Button(onClick = { Svg.writePlanView() }) {
                    Text("Write SVG")
                }
                ReloadButton()
                MessageButton(
                    messagesDisplay,
                    toggle = {
                        messagesDisplay = !messagesDisplay
//                        PipeManager.display = messagesDisplay
                    },
                    displayStateOff = DisplayState(Color.White, Color.Black, "Show Messages"),
                    displayStateOn = DisplayState(Color.Cyan, Color.Black, "Hide Messages"),
                )
                ViewButton(
                    viewSection,
                    toggle = {
                        viewSection = !viewSection
                    },
                    displayStateOff = DisplayState(Color.White, Color.Black, "Show Section"),
                    displayStateOn = DisplayState(Color.Cyan, Color.Black, "Show Plan"),
                )
                FlareVenueButton(
                    flareVenueCorners,
                    toggle = {
                        flareVenueCorners = !flareVenueCorners
                    },
                    displayStateOff = DisplayState(Color.White, Color.Black, "Flare Venue Corners"),
                    displayStateOn = DisplayState(Color.Magenta, Color.Black, "Hide Venue Corners"),
                )
            }
            Display.display(
                drawingWalls,
                share = pipeDisplay || messagesDisplay,
                x, y,
                viewSection = viewSection,
                flareVenueCorners = flareVenueCorners,
            )
            if (messagesDisplay) {
                MessageSideBar.messageLister()
            }
            if (pipeDisplay) {
                PipeSideBar.pipeLister(PipeManager.list)
//        PipeSideBar.pipeLister(PipeManager.list, PipeManager::makeCurrent)
            }

        }
    }
}

var title = ""

fun load() {
    Startup.startup("${Configuration.plotDirectory}/${Configuration.plotFilename}")
    title = try {
        Venue.instances[0].building + " - " + Venue.instances[0].room
    } catch (exception: Exception) {
        "Missing venue"
    }

    PipeManager.buildList(0)
    Svg.writeAll()
    Html.writeAll()
}

fun reload() {
    Startup.clear()

    title = ""
    PipeManager.clear()
    MessageMinder.Messages.clear()
    load()
}


fun main() = application {
    Configuration
    load()
//  exitApplication()

    val state = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(onCloseRequest = ::exitApplication, title = title, state = state) {
        App()
    }
}
