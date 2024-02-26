package pt.isel.pdm.gomokuroyale.http

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pt.isel.pdm.gomokuroyale.http.services.users.UserService
import pt.isel.pdm.gomokuroyale.http.services.users.dto.UserCreateInputModel
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.util.isFailure
import pt.isel.pdm.gomokuroyale.util.onFailureResult

@OptIn(ExperimentalCoroutinesApi::class)
class HTTPServiceTests {

    @get:Rule
    val rule = MockWebServerRule()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = TestScope(UnconfinedTestDispatcher()),
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )

    @Test
    fun `test register`() = runTest {
        // Arrange
        val registerDTO = UserCreateInputModel(
            username = "username",
            email = "email@gmail.com",
            password = "Password123"
        )
        rule.webServer.enqueue(
            MockResponse()
                .setResponseCode(201)
                .addHeader("Content-Type", "application/vnd.siren+json")
                .setBody(
                    "{\n" +
                            "    \"class\": [\n" +
                            "        \"register\"\n" +
                            "    ],\n" +
                            "    \"properties\": {\n" +
                            "        \"uid\": 2\n" +
                            "    },\n" +
                            "    \"links\": [\n" +
                            "        {\n" +
                            "            \"rel\": [\n" +
                            "                \"self\"\n" +
                            "            ],\n" +
                            "            \"href\": \"/api/users\"\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"entities\": [],\n" +
                            "    \"actions\": [],\n" +
                            "    \"requireAuth\": [\n" +
                            "        false\n" +
                            "    ]\n" +
                            "}"
                )
        )
        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString(),
            uriRepository = UriDataStore(testDataStore)
        )
        // Act
        val actual = runBlocking {
            sut.register(registerDTO.username, registerDTO.email, registerDTO.password)
        }

        // Assert
        when (actual) {
            is HttpResult.Success -> {
                assertEquals(2, actual.value.uid)
            }
            is HttpResult.Failure -> {
                assertEquals("Register link not found", actual.error.message)
            }
        }
    }

    @Test
    fun `fetch request on API returns a failure with APIError`() {
        //Arrange
        rule.webServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        val sut = UserService(
            client = rule.httpClient,
            gson = rule.gson,
            apiEndpoint = rule.webServer.url("/").toString(),
            uriRepository = UriDataStore(testDataStore)
        )

        //Act
        runBlocking {
            var msg: String? = null
            val res = sut.getRankingInfo(1).onFailureResult { msg = it.message }
            assertTrue(res.isFailure())
            assertNotNull(msg)
            assertEquals("Ranking link not found", msg)
        }
    }
}