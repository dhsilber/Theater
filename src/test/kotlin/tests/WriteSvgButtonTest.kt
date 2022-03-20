package tests

import App
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mobiletheatertech.plot.Svg
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import kotlin.test.AfterTest

class WriteSvgButtonTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @AfterTest
  fun cleanup() {
    unmockkObject(Svg)
  }

// Most of ui-test is not yet implemented:
// https://github.com/JetBrains/compose-jb/issues/1177

  @Test
  fun testButtonBehavior() {
    composeTestRule.setContent {
      App()
    }

    val button = composeTestRule.onNodeWithText("Write SVG")
    button.assertExists()

    mockkObject(Svg)
    every { Svg.write() } returns Unit
    button.performClick()
    verify { Svg.write() }
  }

}