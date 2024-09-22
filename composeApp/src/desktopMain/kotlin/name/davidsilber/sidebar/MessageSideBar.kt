package sidebar

import MessageMinder
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MessageSideBar {
    companion object {
         @OptIn(ExperimentalFoundationApi::class)
         @Composable
         fun messageLister() {
             Column(
                 modifier = Modifier
                     .padding(20.dp)
                     .border(BorderStroke(2.dp, Color.Red))
                     .fillMaxSize()
                     .verticalScroll(rememberScrollState())
             ) {
                 Text("XML Messages")
                 MessageMinder.Messages.map {
                     Text( it )
                 }
             }
         }
    }
}