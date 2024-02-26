package pt.isel.pdm.gomokuroyale.ui.game.lobby

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyActivity
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyFindGameButtonTestTag
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyPlayerInfoTestTag
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.NavigateBackTestTag

class LobbyActivityTests {

    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun lobby_screen_is_displayed() {
        //Arrange
        ActivityScenario.launch(LobbyActivity::class.java).use {
            //Act
            //Assert
            testRule.onNodeWithTag(LobbyScreenTestTag).assertExists()
            testRule.onNodeWithTag(NavigateBackTestTag).assertExists()
            testRule.onNodeWithTag(LobbyPlayerInfoTestTag).assertExists()
            testRule.onNodeWithTag(LobbyFindGameButtonTestTag).assertExists()
            testRule.onNodeWithTag(LobbyFindGameButtonTestTag).assertIsEnabled()
        }
    }

    @Test
    fun can_go_back_to_main_activity() {
        //Arrange
        ActivityScenario.launch(LobbyActivity::class.java).use { scenario ->
            //Act
            testRule.onNodeWithTag(NavigateBackTestTag).performClick()
            //Assert
            scenario.onActivity { it.isFinishing }
        }
    }

//    @Test
//     fun can_go_to_matchmaker_activity() {
//        //Arrange
//        ActivityScenario.launch(LobbyActivity::class.java).use { scenario ->
//            //Act
//            testRule.onNodeWithTag(LobbyFindGameButtonTestTag).performClick()
//            //Assert
//            scenario.onActivity { it.recreate() }
//            testRule.onNodeWithTag(MatchMakingScreenTestTag).assertExists()
//        }
//    }
}