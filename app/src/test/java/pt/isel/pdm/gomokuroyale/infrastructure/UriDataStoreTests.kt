package pt.isel.pdm.gomokuroyale.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore

@OptIn(ExperimentalCoroutinesApi::class)
class UriDataStoreTests {
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )

    @Test
    fun getRecipeLink_returns_null_if_no_recipe_link_is_stored(): Unit = runTest {
        // Arrange
        val sut = UriDataStore(testDataStore)
        // Act
        val recipeLink = sut.getRecipeLink("self")
        // Assert
        assertEquals(null, recipeLink)
    }

    @Test
    fun updateRecipeLinks_updates_the_recipe_links(): Unit = runTest {
        // Arrange
        val sut = UriDataStore(testDataStore)
        val recipeLinks = listOf(
            Recipe("self", "http://localhost:8080/recipes/self"),
            Recipe("login", "http://localhost:8080/recipes/login"),
            Recipe("logout", "http://localhost:8080/recipes/logout"),
            Recipe("register", "http://localhost:8080/recipes/register"),
            Recipe("games", "http://localhost:8080/recipes/games"),
            Recipe("game", "http://localhost:8080/recipes/game"),
            Recipe("play", "http://localhost:8080/recipes/play"),
            Recipe("join", "http://localhost:8080/recipes/join"),
            Recipe("leave", "http://localhost:8080/recipes/leave"),
            Recipe("move", "http://localhost:8080/recipes/move"),
            Recipe("watch", "http://localhost:8080/recipes/watch"),
            Recipe("unwatch", "http://localhost:8080/recipes/unwatch"),
            Recipe("ranking", "http://localhost:8080/recipes/ranking"),
            Recipe("ranking", "http://localhost:8080/recipes/ranking")
        )
        // Act
        val result = sut.updateRecipeLinks(recipeLinks)
        // Assert
        assertEquals(recipeLinks, result)

        testDataStore.edit { it.clear() }
    }
}