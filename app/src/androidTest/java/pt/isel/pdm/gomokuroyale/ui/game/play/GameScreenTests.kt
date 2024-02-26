package pt.isel.pdm.gomokuroyale.ui.game.play

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.domain.Email
import pt.isel.pdm.gomokuroyale.authentication.domain.Id
import pt.isel.pdm.gomokuroyale.authentication.domain.User
import pt.isel.pdm.gomokuroyale.game.play.domain.Game
import pt.isel.pdm.gomokuroyale.game.play.domain.board.Board
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.game.play.ui.GameExitButtonTestTag
import pt.isel.pdm.gomokuroyale.game.play.ui.GameHelpButtonTestTag
import pt.isel.pdm.gomokuroyale.game.play.ui.GameScreen
import pt.isel.pdm.gomokuroyale.game.play.ui.GameScreenState
import pt.isel.pdm.gomokuroyale.game.play.ui.GameScreenTestTag

class GameScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGameScreen() {
        //Arrange
        composeTestRule.setContent {
            GameScreen(GameScreenState.Loading)
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(GameScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(GameExitButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(GameHelpButtonTestTag).assertExists()
        composeTestRule.onNodeWithTag(GameExitButtonTestTag).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(GameHelpButtonTestTag).assertIsEnabled()
    }

    @Test
    fun testGameScreenExitButton() {
        //Arrange
        var exit = false
        composeTestRule.setContent {
            GameScreen(
                state = GameScreenState.Playing(
                    game = Game(
                        id = 1,
                        userBlack = mockUser1,
                        userWhite = mockUser2,
                        board = Board.Companion.EMPTY,
                        state = "PLAYER_BLACK_TURN",
                        variant = variant,
                    )
                ) ,
                onForfeitRequested = { exit = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(GameExitButtonTestTag).performClick()
        //Assert
        assert(exit)
    }

    @Test
    fun testGameScreenHelpButton() {
        //Arrange
        var help = false
        composeTestRule.setContent {
            GameScreen(
                state = GameScreenState.Playing(
                    game = Game(
                        id = 1,
                        userBlack = mockUser1,
                        userWhite = mockUser2,
                        board = Board.Companion.EMPTY,
                        state = "PLAYER_BLACK_TURN",
                        variant = variant,
                    )
                ) ,
                onHelpRequested = { help = true }
            )
        }
        //Act
        composeTestRule.onNodeWithTag(GameHelpButtonTestTag).performClick()
        //Assert
        assert(help)
    }

    companion object{
        val mockUser1 = User(
            id = Id(1),
            username = "user1",
            email = Email("mock1@example.com"),
        )

        val mockUser2 = User(
            id = Id(2),
            username = "user2",
            email = Email("mock2@example.com"),
        )

        val variant = Variant(
            name = "Standard",
            boardDim = 15,
            playRule = "Standard",
            openingRule = "Standard",
            points = 5
        )
    }
}