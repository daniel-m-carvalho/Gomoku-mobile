package pt.isel.pdm.gomokuroyale.ui

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfo
import pt.isel.pdm.gomokuroyale.authentication.domain.UserInfoRepository
import pt.isel.pdm.gomokuroyale.game.lobby.domain.VariantRepository
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.media.siren.LinkModel
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenState
import pt.isel.pdm.gomokuroyale.main.ui.MainScreenViewModel
import pt.isel.pdm.gomokuroyale.util.HttpResult
import pt.isel.pdm.gomokuroyale.utils.MockMainDispatcherRule
import pt.isel.pdm.gomokuroyale.utils.SuspendingGate

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTests {

    @get:Rule
    val rule = MockMainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val testUserInfo = UserInfo("test", "test")
    private val userInfoRepo = mockk<UserInfoRepository> {
        coEvery { getUserInfo() } coAnswers { testUserInfo }
    }

    private val testUris = listOf(
        Recipe("self", "http://localhost:8080/"),
        Recipe("login", "http://localhost:8080/login"),
        Recipe("logout", "http://localhost:8080/logout"),
        Recipe("register", "http://localhost:8080/register"),
        Recipe("game-lobby", "http://localhost:8080/game-lobby"),
        Recipe("game-play", "http://localhost:8080/game-play"),
    )
    private val uriRepo = mockk<UriRepository> {
        coEvery { updateRecipeLinks(testUris) } coAnswers { testUris }
    }

    private val getHomeList = listOf(
        LinkModel(listOf( "self" ), "http://localhost:8080/api/"),
        LinkModel( listOf( "login"), "http://localhost:8080/api/login"),
        LinkModel( listOf( "logout"), "http://localhost:8080/api/logout"),
        LinkModel( listOf( "register"), "http://localhost:8080/api/register"),
        LinkModel( listOf( "game-lobby"), "http://localhost:8080/api/game-lobby"),
        LinkModel( listOf( "game-play"), "http://localhost:8080/api/game-play"),
    )
    private val gomokuService = mockk<GomokuService> {
        coEvery { getHome() } coAnswers { HttpResult.Success(getHomeList) }
    }

    private val testVariants = listOf(
        Variant("STANDARD", 15, "Standard", "Standard", 5),
        Variant("Pente", 15, "Standard", "Standard", 5),
        Variant("RENJU", 15, "Standard", "Standard", 5),
    )
    private val variantRepo = mockk<VariantRepository> {
        coEvery { getVariants() } coAnswers { testVariants }
    }

    @Test
    fun initially_the_state_flow_is_idle() = runTest {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo, uriRepo, gomokuService,variantRepo)
        // Act
        val gate = SuspendingGate()
        var collectedState: MainScreenState? = null
        val collectJob = launch {
            sut.state.collect {
                collectedState = it
                gate.open()
            }
        }

        // Lets wait for the flow to emit the first value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        assertTrue("Expected Idle bot got $collectedState instead", collectedState is MainScreenState.Idle)
    }

    @Test
    fun fetchRecipes_emits_to_the_state_flow_the_fetchedRecipes_state() = runTest {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo, uriRepo, gomokuService,variantRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: MainScreenState? = null
        val collectJob = launch {
            sut.state.collectLatest {
                if (it is MainScreenState.FetchedRecipes) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.updateRecipes()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loaded = lastCollectedState as? MainScreenState.FetchedRecipes
        assertNotNull("Expected Loaded but got $lastCollectedState instead", loaded)
    }

    @Test
    fun fetchRecipes_emits_to_the_state_flow_the_fetchingRecipes_state() = runTest {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo, uriRepo, gomokuService,variantRepo)
        // Act
        val gate = SuspendingGate()
        var lastCollectedState: MainScreenState? = null
        val collectJob = launch {
            sut.state.collect {
                if (it is MainScreenState.FetchingRecipes) {
                    lastCollectedState = it
                    gate.open()
                }
            }
        }
        sut.updateRecipes()

        // Lets wait for the flow to emit the latest value
        withTimeout(1000) {
            gate.await()
            collectJob.cancelAndJoin()
        }

        // Assert
        val loading = lastCollectedState as? MainScreenState.FetchingRecipes
        assertNotNull("Expected Loading but got $lastCollectedState instead", loading)
    }

    @Test(expected = IllegalStateException::class)
    fun fetchUserInfo_throws_if_state_is_idle() {
        // Arrange
        val sut = MainScreenViewModel(userInfoRepo, uriRepo, gomokuService,variantRepo)
        sut.fetchPlayerInfo()

        // Act
        sut.fetchPlayerInfo()
    }
}