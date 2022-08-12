package tests

import ui.WallButton
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class WallButtonTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testButtonText() {
    composeTestRule.setContent {
      var drawingWalls = false
//      Text("Wall")
      WallButton(drawingWalls, {})
    }

    val button = composeTestRule.onNodeWithText("Wall")

    button.assertExists()
//    button.assertIsDisplayed()  // Not implemented
//    button.assertDoesNotExist()  // not true
  }

  // Fails because printToLog is not implemented as of 2022-03-20
  // found printToLog in https://developer.android.com/jetpack/compose/testing
//  @Test
//  fun `button is red when drawingWalls is true`() {
//    composeTestRule.setContent {
//      var drawingWalls = false
//      WallButton(drawingWalls, {} )
//    }
//
////    val button = composeTestRule.onNodeWithText("Wall")
//
//    composeTestRule.onRoot().printToLog("TAG")
//  }

  @Test
  fun `button invokes function on click`() {
    var drawingWalls: Boolean = false
    composeTestRule.setContent {
      WallButton(drawingWalls, { drawingWalls = !drawingWalls })
    }
    val button = composeTestRule.onNodeWithText("Wall")

    button.performClick()

    assertTrue(drawingWalls)
  }

}
