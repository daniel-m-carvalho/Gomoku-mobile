package pt.isel.pdm.gomokuroyale.ui.authentication.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginActivity
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.components.ButtonTestTag
import pt.isel.pdm.gomokuroyale.ui.components.VerificationTestTag


class LoginActivityTests {

    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun login_screen_is_displayed () {
        // Arrange
        ActivityScenario.launch(LoginActivity::class.java).use {
            // Act
            // Assert
            testRule.onNodeWithTag(LoginScreenTestTag).assertExists()
            testRule.onNodeWithTag(ButtonTestTag).assertExists()
            testRule.onNodeWithTag(VerificationTestTag).assertExists()
        }
    }

    @Test
    fun can_go_back_to_main_activity () {
        // Arrange
        ActivityScenario.launch(LoginActivity::class.java).use { scenario ->
            // Act
            testRule.onNodeWithTag(ButtonTestTag).performClick()
            // Assert
            scenario.onActivity { it.isFinishing }
        }
    }
}