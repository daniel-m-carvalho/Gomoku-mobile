package pt.isel.pdm.gomokuroyale.ui.game.matchMake

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchMakingCancelButtonTestTag
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchMakingScreenTestTag
import pt.isel.pdm.gomokuroyale.game.matchmake.ui.MatchmakerScreen
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.domain.MatchmakingStatus
import pt.isel.pdm.gomokuroyale.ui.TipInfoBoxTestTag

class MatchMakingScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMatchMakingScreenPending() {
        //Arrange
        composeTestRule.setContent {
            MatchmakerScreen(
                status= MatchmakingStatus.PENDING,
                variant = variant
            )
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(MatchMakingScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(MatchMakingCancelButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(TipInfoBoxTestTag).assertExists()
        composeTestRule.onNodeWithTag(MatchMakingCancelButtonTestTag).assertIsEnabled()
    }

    @Test
    fun testMatchMakingScreenMatched() {
        //Arrange
        composeTestRule.setContent {
            MatchmakerScreen(
                status= MatchmakingStatus.MATCHED,
                variant = variant
            )
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(MatchMakingScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(MatchMakingCancelButtonTestTag).assertDoesNotExist()
        composeTestRule.onNodeWithTag(TipInfoBoxTestTag).assertExists()
    }

    @Test
    fun testMatchMakingScreenMatchedWithCancelingEnabled() {
        //Arrange
        var canceling = false
        composeTestRule.setContent {
            MatchmakerScreen(
                status= MatchmakingStatus.PENDING,
                variant = variant,
                onCancelingMatchmaking = { canceling = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(MatchMakingCancelButtonTestTag).performClick()
        //Assert
        assert(canceling)
    }

    companion object {
        val variant = Variant(
            name = "Standard",
            boardDim = 15,
            playRule = "Standard",
            openingRule = "Standard",
            points = 5
        )
    }
}