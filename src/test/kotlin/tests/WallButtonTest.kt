package tests

import WallButton
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class WallButtonTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testButtonText() {
    composeTestRule.setContent {
//      Text("Wall")
      WallButton()
    }

    val button = composeTestRule.onNodeWithText("Wall")

    button.assertExists()
//    button.assertIsDisplayed()  // Not implemented
//    button.assertDoesNotExist()  // not true
  }

}