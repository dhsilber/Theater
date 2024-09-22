/*

In migrating from org.jetbrains.compose 1.1.0 to 1.2.0-alpha01-dev755,
functions for writing tests against compose methods vanished.

The Compose part of this project is cool, but tests here were already very minimal
and I don't have time to fight with the missing infrastructure right now.

 */

//package tests
//
//import ui.WallButton
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onRoot
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.printToLog
//import org.junit.Assert.assertThat
//import org.junit.Rule
//import org.junit.Test
//import kotlin.test.assertTrue
//
//class WallButtonTest {
//  @get:Rule
//  val composeTestRule = createComposeRule()
//
//  @Test
//  fun testButtonText() {
//    composeTestRule.setContent {
//      var drawingWalls = false
////      Text("Wall")
//      WallButton(drawingWalls, {})
//    }
//
//    val button = composeTestRule.onNodeWithText("Wall")
//
//    button.assertExists()
////    button.assertIsDisplayed()  // Not implemented
////    button.assertDoesNotExist()  // not true
//  }
//
//  // Fails because printToLog is not implemented as of 2022-03-20
//  // found printToLog in https://developer.android.com/jetpack/compose/testing
////  @Test
////  fun `button is red when drawingWalls is true`() {
////    composeTestRule.setContent {
////      var drawingWalls = false
////      WallButton(drawingWalls, {} )
////    }
////
//////    val button = composeTestRule.onNodeWithText("Wall")
////
////    composeTestRule.onRoot().printToLog("TAG")
////  }
//
//  @Test
//  fun `button invokes function on click`() {
//    var drawingWalls: Boolean = false
//    composeTestRule.setContent {
//      WallButton(drawingWalls, { drawingWalls = !drawingWalls })
//    }
//    val button = composeTestRule.onNodeWithText("Wall")
//
//    button.performClick()
//
//    assertTrue(drawingWalls)
//  }
//
//}
