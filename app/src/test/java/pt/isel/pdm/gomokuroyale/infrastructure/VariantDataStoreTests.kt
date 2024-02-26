package pt.isel.pdm.gomokuroyale.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pt.isel.pdm.gomokuroyale.game.lobby.storage.VariantDataStore
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant

@OptIn(ExperimentalCoroutinesApi::class)
class VariantDataStoreTests {
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )

    private val gson = Gson()

    @Test
    fun getVariants_returns_empty_list_if_no_variants_are_stored(): Unit = runTest {
        // Arrange
        val sut = VariantDataStore(testDataStore, gson)
        // Act
        val variants = sut.getVariants()
        // Assert
        assertEquals(emptyList<Variant>(), variants)
    }

    @Test
    fun getVariants_returns_the_previously_stored_value(): Unit = runTest {
        // Arrange
        val sut = VariantDataStore(testDataStore, gson)
        val expected = listOf(
            Variant(
                "STANDARD",
                15,
                "Standard",
                "Standard",
                5
            ),
            Variant(
                "Pente",
                15,
                "Standard",
                "Standard",
                5
            ),
            Variant(
                "RENJU",
                15,
                "Standard",
                "Standard",
                5
            )
        )
        sut.storeVariants(expected)
        // Act
        val result = sut.getVariants()
        // Assert
        assertEquals(expected, result)

        testDataStore.edit {  it.clear() }
    }
}