package pt.isel.pdm.gomokuroyale.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.theme.DarkViolet
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val MainScreenTestTag = "MainScreenTestTag"
const val PlayButtonTestTag = "PlayButtonTestTag"
const val RankingButtonTestTag = "RankingButtonTestTag"
const val RegisterButtonTestTag = "RegisterButtonTestTag"
const val LoginButtonTestTag = "LoginButtonTestTag"
const val LogoutButtonTestTag = "LogoutButtonTestTag"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    isLoggedIn: Boolean = false,
    onCreateGameRequested: () -> Unit = {},
    onInfoRequested: () -> Unit = {},
    onRankingRequested: () -> Unit = {},
    onLoginRequested: () -> Unit = {},
    onRegisterRequested: () -> Unit = {},
    onLogoutRequested: () -> Unit = {},
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(MainScreenTestTag),
            topBar = {
                TopBar(
                    navigation = NavigationHandlers(
                        onInfoRequested = onInfoRequested,
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    painter = painterResource(id = R.drawable.gomoku),
                    contentDescription = stringResource(id = R.string.app_menu_name)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.app_menu_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.size(30.dp))
                    ButtonMenu(icon = Icons.Default.PlayArrow, stringId = R.string.play_title, testTag = PlayButtonTestTag) {
                        onCreateGameRequested()
                    }
                    ButtonMenu(icon = Icons.Default.List, stringId = R.string.ranking_menu_title, testTag = RankingButtonTestTag) {
                        onRankingRequested()
                    }
                    if (!isLoggedIn) {
                        ButtonMenu(
                            icon = Icons.Default.AccountBox,
                            stringId = R.string.register_title,
                            testTag = RegisterButtonTestTag
                        ) {
                            onRegisterRequested()
                        }
                        ButtonMenu(icon = Icons.Default.Person, stringId = R.string.login_title, testTag = LoginButtonTestTag) {
                            onLoginRequested()
                        }
                    } else
                        ButtonMenu(icon = Icons.Default.ExitToApp, stringId = R.string.logout, testTag = LogoutButtonTestTag) {
                            onLogoutRequested()
                        }
                }
            }
        }
    }
}

@Composable
private fun ButtonMenu(icon: ImageVector, stringId: Int, testTag: String = "",onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(0.8f).testTag(testTag),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(DarkViolet)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = stringId)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(stringResource(id = stringId), textAlign = TextAlign.Start)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}