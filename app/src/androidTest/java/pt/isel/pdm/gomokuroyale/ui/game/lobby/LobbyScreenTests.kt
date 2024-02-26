package pt.isel.pdm.gomokuroyale.ui.game.lobby

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.lobby.domain.PlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyFindGameButtonTestTag
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyPlayerInfoTestTag
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyScreen
import pt.isel.pdm.gomokuroyale.game.lobby.ui.LobbyScreenTestTag
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.ui.NavigateBackTestTag

class LobbyScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLobbyScreen() {
        //Arrange
        composeTestRule.setContent {
            LobbyScreen(
                variants = variants,
                playerInfo = playerInfo
            )
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(LobbyScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(NavigateBackTestTag).assertExists()
        composeTestRule.onNodeWithTag(LobbyPlayerInfoTestTag).assertExists()
        composeTestRule.onNodeWithTag(LobbyFindGameButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(LobbyFindGameButtonTestTag).assertIsEnabled()
    }

    @Test
    fun testLobbyScreenFindGameButton() {
        //Arrange
        var findGame = false
        composeTestRule.setContent {
            LobbyScreen(
                variants = variants,
                playerInfo = playerInfo,
                onFindGame = { findGame = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(LobbyFindGameButtonTestTag).performClick()
        //Assert
        assert(findGame)
    }

    companion object{
        val variants = listOf(
        Variant("STANDARD", 15, "Standard", "Standard", 5),
        Variant("Pente", 15, "Standard", "Standard", 5),
        Variant("RENJU", 15, "Standard", "Standard", 5),
        )
        val playerInfo = PlayerInfo("Player Name", 1000)
    }
}