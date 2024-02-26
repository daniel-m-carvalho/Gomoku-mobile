package pt.isel.pdm.gomokuroyale.game.lobby.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.game.lobby.domain.PlayerInfo
import pt.isel.pdm.gomokuroyale.game.lobby.domain.Variants
import pt.isel.pdm.gomokuroyale.game.play.domain.variants.Variant
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme
import pt.isel.pdm.gomokuroyale.ui.theme.Purple40
import pt.isel.pdm.gomokuroyale.ui.theme.Purple80
import pt.isel.pdm.gomokuroyale.ui.theme.PurpleGrey40
import pt.isel.pdm.gomokuroyale.ui.theme.PurpleGrey80
import pt.isel.pdm.gomokuroyale.ui.theme.Violet

const val LobbyScreenTestTag = "LOBBY_SCREEN_TEST_TAG"
const val LobbyFindGameButtonTestTag = "LOBBY_SCREEN_FIND_GAME_BUTTON_TEST_TAG"
const val LobbyPlayerInfoTestTag = "LOBBY_SCREEN_PLAYER_INFO_TEST_TAG"

@Composable
fun LobbyScreen(
    modifier: Modifier = Modifier,
    variants: List<Variant>,
    onPlayEnabled: Boolean = true,
    onFindGame: (String) -> Unit = {},
    playerInfo: PlayerInfo? = null,
    onNavigationBackRequested: () -> Unit = {}
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LobbyScreenTestTag),
            topBar = {
                TopBar(
                    navigation = NavigationHandlers(onBackRequested = onNavigationBackRequested)
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Violet),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerLobbyInfo(playerInfo, modifier)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(DarkViolet),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var selectedVariant by remember { mutableStateOf(Variants.Standard.value) }
                    val listOfVariants = variants.map {
                        ToggleableInfo(
                            isChecked = it.name == Variants.Standard.value,
                            variant = it,
                            text = it.name
                        )
                    }
                    val radioButtons = remember {
                        mutableStateListOf(*listOfVariants.toTypedArray())
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.variant),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (radioButtons.isEmpty())
                            radioButtons.addAll(listOfVariants)
                        else {
                            val listSplitter = splitList(radioButtons)
                            Column(horizontalAlignment = Alignment.Start) {
                                ListRadioButtons(listSplitter.first, radioButtons)
                            }
                            Column(horizontalAlignment = Alignment.Start) {
                                ListRadioButtons(listSplitter.second, radioButtons)
                            }
                            selectedVariant = radioButtons.first { it.isChecked }.variant.name
                        }
                    }
                    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
                        GradientButton(enabled = onPlayEnabled) { onFindGame(selectedVariant) }
                    }
                }
            }
        }
    }
}

@Composable
private fun GradientButton(enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
            .testTag(LobbyFindGameButtonTestTag),
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(colors = gradientColors),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.play_find_game),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private val gradientColors = listOf(
    Purple80, PurpleGrey40, Purple40, PurpleGrey80
)

@Composable
private fun PlayerLobbyInfo(playerInfo: PlayerInfo?, modifier: Modifier) {
    Image(
        modifier = modifier
            .padding(top = 16.dp)
            .clip(CircleShape)
            .background(Color.White)
            .testTag(LobbyPlayerInfoTestTag),
        painter = painterResource(id = R.drawable.chapeleiro_louco),
        contentDescription = "Player Icon"
    )
    Text(
        modifier = modifier,
        text = playerInfo?.username ?: "",
        style = MaterialTheme.typography.titleLarge
    )
    //Text(modifier = modifier, text = playerInfo?.points.toString())
}

@Composable
private fun ListRadioButtons(
    listSplit: List<ToggleableInfo>,
    radioButtons: SnapshotStateList<ToggleableInfo>,
) {
    listSplit.forEachIndexed { _, info ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    radioButtons.replaceAll {
                        it.copy(
                            isChecked = it.text == info.text
                        )
                    }
                }
                .padding(end = 16.dp)
        ) {
            RadioButton(
                selected = info.isChecked,
                onClick = {
                    radioButtons.replaceAll {
                        it.copy(
                            isChecked = it.text == info.text
                        )
                    }
                }
            )
            Text(text = info.text)
        }
    }
}

private data class ToggleableInfo(
    val isChecked: Boolean,
    val variant: Variant,
    val text: String
)

fun <T> splitList(inputList: MutableList<T>): Pair<MutableList<T>, MutableList<T>> {
    val size = inputList.size
    val middle = size / 2
    val firstHalf = inputList.subList(0, middle + size % 2)
    val secondHalf = inputList.subList(middle + size % 2, size)
    return Pair(firstHalf, secondHalf)
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    LobbyScreen(
        variants = listOf(
            Variant("STANDARD", 15, "Standard", "Standard", 5),
            Variant("Pente", 15, "Standard", "Standard", 5),
            Variant("RENJU", 15, "Standard", "Standard", 5),
        ), playerInfo = PlayerInfo("Player Name", 1000)
    )
}
