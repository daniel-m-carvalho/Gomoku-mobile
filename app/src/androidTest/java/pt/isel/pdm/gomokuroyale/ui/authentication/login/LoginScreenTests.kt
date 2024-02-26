package pt.isel.pdm.gomokuroyale.ui.authentication.login

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreen
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.components.ButtonTestTag
import pt.isel.pdm.gomokuroyale.ui.components.VerificationTestTag

class LoginScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreen() {
        //Arrange
        composeTestRule.setContent {
            LoginScreen(
                onBackRequested = {},
                onRegisterRequested = {},
                onLoginRequested = { _, _ -> }
            )
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(LoginScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(ButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(VerificationTestTag).assertExists()
    }

}