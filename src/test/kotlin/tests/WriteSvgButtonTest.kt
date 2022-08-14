/*

In migrating from org.jetbrains.compose 1.1.0 to 1.2.0-alpha01-dev755,
functions for writing tests against compose methods vanished.

The Compose part of this project is cool, but tests here were already very minimal
and I don't have time to fight with the missing infrastructure right now.

 */

//package tests
//
//import App
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import Svg
//import io.mockk.every
//import io.mockk.mockkObject
//import io.mockk.unmockkObject
//import io.mockk.verify
//import org.junit.Rule
//import org.junit.Test
//import kotlin.test.AfterTest
//
//class WriteSvgButtonTest {
//  @get:Rule
//  val composeTestRule = createComposeRule()
//
//  @AfterTest
//  fun cleanup() {
//    unmockkObject(Svg)
//  }
//
//// Most of ui-test is not yet implemented:
//// https://github.com/JetBrains/compose-jb/issues/1177
//
//  @Test
//  fun testButtonBehavior() {
//    composeTestRule.setContent {
//      App()
//    }
//
//    val button = composeTestRule.onNodeWithText("Write SVG")
//    button.assertExists()
//
//    mockkObject(Svg)
//    every { Svg.writePlanView() } returns Unit
//    button.performClick()
//    verify { Svg.writePlanView() }
//  }
//
//}
