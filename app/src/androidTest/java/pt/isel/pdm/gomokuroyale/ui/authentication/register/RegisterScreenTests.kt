package pt.isel.pdm.gomokuroyale.ui.authentication.register

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterCheckBoxTestTag
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterScreen
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.components.ButtonTestTag
import pt.isel.pdm.gomokuroyale.ui.components.VerificationTestTag

class RegisterScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRegisterScreen() {
        //Arrange
        composeTestRule.setContent {
            RegisterScreen(
                onBackRequested = {},
                onRegisterRequested = {_, _, _ ->},
                onLoginActivity = {}
            )
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(RegisterScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(RegisterCheckBoxTestTag).assertExists()
        composeTestRule.onNodeWithTag(ButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(VerificationTestTag).assertExists()
    }
}