package pt.isel.pdm.gomokuroyale.ui.matchHistory

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Piece
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchInfo
import pt.isel.pdm.gomokuroyale.matchHistory.ui.Result
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryListTestTag
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryScreen
import pt.isel.pdm.gomokuroyale.matchHistory.ui.MatchHistoryScreenTestTag
import pt.isel.pdm.gomokuroyale.ui.NavigateBackTestTag

class MatchHistoryScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testMatchHistoryScreen() {
        //Arrange
        composeTestRule.setContent {
            MatchHistoryScreen(
                username = "username",
                matches = matchesList,
            )
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(MatchHistoryScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(MatchHistoryListTestTag).assertExists()
        composeTestRule.onNodeWithTag(NavigateBackTestTag).assertExists()
    }

    companion object {
        val matchesList = buildList {
            repeat(15) {
                if(it%2 == 0)
                    if (it == 4)
                        add(MatchInfo(Result.Win, "STANDARD", "Opponent", Piece.WHITE))
                    else
                        add(MatchInfo(Result.Win, "STANDARD", "Opponent", Piece.BLACK))
                else {
                    if (it == 1)
                        add(MatchInfo(Result.Loss, "STANDARD", "Opponent", Piece.BLACK))
                    else
                        add(MatchInfo(Result.Loss, "STANDARD", "Opponent", Piece.WHITE))
                }

            }
        }
    }
}