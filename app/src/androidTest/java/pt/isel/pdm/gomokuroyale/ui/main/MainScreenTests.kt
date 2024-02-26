package pt.isel.pdm.gomokuroyale.ui.main

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.main.ui.LoginButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.LogoutButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.MainScreen
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenTestTag
import pt.isel.pdm.gomokuroyale.main.ui.PlayButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.RankingButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.RegisterButtonTestTag
import pt.isel.pdm.gomokuroyale.ui.NavigateToInfoTestTag

class MainScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMainScreen() {
        //Arrange
        composeTestRule.setContent {
            MainScreen()
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(MainScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(NavigateToInfoTestTag).assertExists()
        composeTestRule.onNodeWithTag(PlayButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(RankingButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(RegisterButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(LoginButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(LogoutButtonTestTag).assertDoesNotExist()
    }

    @Test
    fun testMainScreenLoggedIn() {
        //Arrange
        composeTestRule.setContent {
            MainScreen(isLoggedIn = true)
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(MainScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(NavigateToInfoTestTag).assertExists()
        composeTestRule.onNodeWithTag(PlayButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(RankingButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(RegisterButtonTestTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(LoginButtonTestTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(LogoutButtonTestTag).assertExists()
    }

    @Test
    fun testMainScreenPlayButton() {
        //Arrange
        var isPlayButtonClicked = false
        composeTestRule.setContent {
            MainScreen(
                onCreateGameRequested = { isPlayButtonClicked = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(PlayButtonTestTag).performClick()
        //Assert
        assertTrue(isPlayButtonClicked)
    }

    @Test
    fun testMainScreenRankingButton() {
        //Arrange
        var isRankingButtonClicked = false
        composeTestRule.setContent {
            MainScreen(
                onRankingRequested = { isRankingButtonClicked = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(RankingButtonTestTag).performClick()
        //Assert
        assertTrue(isRankingButtonClicked)
    }

    @Test
    fun testMainScreenRegisterButton() {
        //Arrange
        var isRegisterButtonClicked = false
        composeTestRule.setContent {
            MainScreen(
                onRegisterRequested = { isRegisterButtonClicked = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(RegisterButtonTestTag).performClick()
        //Assert
        assertTrue(isRegisterButtonClicked)
    }

    @Test
    fun testMainScreenLoginButton() {
        //Arrange
        var isLoginButtonClicked = false
        composeTestRule.setContent {
            MainScreen(
                onLoginRequested = { isLoginButtonClicked = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(LoginButtonTestTag).performClick()
        //Assert
        assertTrue(isLoginButtonClicked)
    }

    @Test
    fun testMainScreenLogoutButton() {
        //Arrange
        var isLogoutButtonClicked = false
        composeTestRule.setContent {
            MainScreen(
                isLoggedIn = true,
                onLogoutRequested = { isLogoutButtonClicked = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(LogoutButtonTestTag).performClick()
        //Assert
        assertTrue(isLogoutButtonClicked)
    }
}