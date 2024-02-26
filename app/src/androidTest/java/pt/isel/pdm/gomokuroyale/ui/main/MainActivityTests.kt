package pt.isel.pdm.gomokuroyale.ui.main

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.about.ui.AboutScreenTestTag
import pt.isel.pdm.gomokuroyale.authentication.ui.login.LoginScreenTestTag
import pt.isel.pdm.gomokuroyale.authentication.ui.register.RegisterScreenTestTag
import pt.isel.pdm.gomokuroyale.main.ui.LoginButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.LogoutButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.MainActivity
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenTestTag
import pt.isel.pdm.gomokuroyale.main.ui.PlayButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.RankingButtonTestTag
import pt.isel.pdm.gomokuroyale.main.ui.RegisterButtonTestTag
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.NavigateToInfoTestTag

class MainActivityTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun main_screen_is_displayed_without_login() {
        //Arrange
        ActivityScenario.launch(MainActivity::class.java).use {
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
    }

    @Test
    fun can_go_to_login_activity() {
        //Arrange
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            //Act
            composeTestRule.onNodeWithTag(LoginButtonTestTag).performClick()
            //Assert
            scenario.onActivity { it.recreate() }
            composeTestRule.onNodeWithTag(LoginScreenTestTag).assertExists()
        }
    }

    @Test
    fun can_go_to_register_activity() {
        //Arrange
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            //Act
            composeTestRule.onNodeWithTag(RegisterButtonTestTag).performClick()
            //Assert
            scenario.onActivity { it.recreate() }
            composeTestRule.onNodeWithTag(RegisterScreenTestTag).assertExists()
        }
    }

    @Test
    fun can_go_to_ranking_activity() {
        //Arrange
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            //Act
            composeTestRule.onNodeWithTag(RankingButtonTestTag).performClick()
            //Assert
            scenario.onActivity { it.recreate() }
            composeTestRule.onNodeWithTag(RankingScreenTestTag).assertExists()
        }
    }

    @Test
    fun can_go_to_info_activity() {
        //Arrange
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            //Act
            composeTestRule.onNodeWithTag(NavigateToInfoTestTag).performClick()
            //Assert
            scenario.onActivity { it.recreate() }
            composeTestRule.onNodeWithTag(AboutScreenTestTag).assertExists()
        }
    }
}