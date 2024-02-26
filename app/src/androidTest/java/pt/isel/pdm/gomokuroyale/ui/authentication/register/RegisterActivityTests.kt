package pt.isel.pdm.gomokuroyale.ui.authentication.register

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.NavigateBackTestTag
import pt.isel.pdm.gomokuroyale.ui.components.ButtonTestTag
import pt.isel.pdm.gomokuroyale.ui.components.VerificationTestTag

class RegisterActivityTests {

    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun register_screen_is_displayed () {
        // Arrange
        ActivityScenario.launch(RegisterActivity::class.java).use {
            // Act
            // Assert
            testRule.onNodeWithTag(RegisterScreenTestTag).assertExists()
            testRule.onNodeWithTag(ButtonTestTag).assertExists()
            testRule.onNodeWithTag(ButtonTestTag).assertIsEnabled()
            testRule.onNodeWithTag(VerificationTestTag).assertExists()
        }
    }

    @Test
    fun can_go_back_to_main_activity () {
        // Arrange
        ActivityScenario.launch(RegisterActivity::class.java).use { scenario ->
            // Act
            testRule.onNodeWithTag(NavigateBackTestTag).performClick()
            // Assert
            scenario.onActivity { it.isFinishing }
        }
    }
}