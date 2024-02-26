package pt.isel.pdm.gomokuroyale.infrastructure

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.storage.UserInfoDataStore

@OptIn(ExperimentalCoroutinesApi::class)
class UserInfoDataStoreTests {
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )

    private val clock = Clock.System

    @Test
    fun getUserInfo_returns_null_if_no_user_info_is_stored(): Unit = runTest {
        // Arrange
        val sut = UserInfoDataStore(testDataStore, clock)
        // Act
        val userInfo = sut.getUserInfo()
        // Assert
        assertNull(userInfo)
    }

    @Test
    fun getUserInfo_returns_the_previously_stored_value_and_clears_in_the_end(): Unit = runTest {
        // Arrange
        val sut = UserInfoDataStore(testDataStore, clock)
        sut.login(UserInfo("Token", "username"))
        val expected = UserInfo("Token", "username")
        // Act
        val result = sut.getUserInfo()
        // Assert
        assertEquals(expected, result)
        assertTrue(sut.isLoggedIn())

        //Act
        sut.logout()
        val userInfo = sut.getUserInfo()
        assertNull(userInfo)
        assertFalse(sut.isLoggedIn())
    }
}