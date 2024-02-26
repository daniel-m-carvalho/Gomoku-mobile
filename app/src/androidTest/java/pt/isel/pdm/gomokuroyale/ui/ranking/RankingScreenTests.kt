package pt.isel.pdm.gomokuroyale.ui.ranking

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingListTestTag
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreen
import pt.isel.pdm.gomokuroyale.rankings.ui.RankingScreenTestTag
import pt.isel.pdm.gomokuroyale.rankings.ui.SearchBarTestTag

class RankingScreenTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRankingScreen() {
        //Arrange
        composeTestRule.setContent {
            RankingScreen()
        }
        //Act
        //Assert
        composeTestRule.onNodeWithTag(RankingScreenTestTag).assertExists()
        composeTestRule.onNodeWithTag(SearchBarTestTag).assertExists()
        composeTestRule.onNodeWithTag(RankingListTestTag).assertExists()
    }
}