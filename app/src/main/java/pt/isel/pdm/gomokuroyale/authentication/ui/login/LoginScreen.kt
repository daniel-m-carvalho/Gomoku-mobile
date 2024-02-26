package pt.isel.pdm.gomokuroyale.authentication.ui.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.domain.validatePassword
import pt.isel.pdm.gomokuroyale.authentication.domain.validateUsername
import pt.isel.pdm.gomokuroyale.ui.NavigationHandlers
import pt.isel.pdm.gomokuroyale.ui.TopBar
import pt.isel.pdm.gomokuroyale.ui.components.ButtonComponent
import pt.isel.pdm.gomokuroyale.ui.components.DivideComponent
import pt.isel.pdm.gomokuroyale.ui.components.FieldType
import pt.isel.pdm.gomokuroyale.ui.components.InformationBox
import pt.isel.pdm.gomokuroyale.ui.components.TextComponent
import pt.isel.pdm.gomokuroyale.ui.components.VerificationComponent
import pt.isel.pdm.gomokuroyale.ui.theme.GomokuRoyaleTheme

const val LoginScreenTestTag = "LoginScreenTestTag"
private val paddingHead = 30.dp

@Composable
fun LoginScreen(
    isLoginButtonEnabled: Boolean = true,
    onBackRequested: () -> Unit = { },
    onRegisterRequested: () -> Unit = { },
    onLoginRequested: (username: String, password: String) -> Unit
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(LoginScreenTestTag),
            topBar = { TopBar(navigation = NavigationHandlers(onBackRequested = onBackRequested)) },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingHead)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    var username by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }
                    var isUsernameValid by rememberSaveable { mutableStateOf(true) }
                    var isPasswordValid by rememberSaveable { mutableStateOf(true) }

                    TextComponent(R.string.login_title)
                    InformationBox(
                        label = "Username",
                        value = username,
                        onValueChange = {
                            username = it
                        },
                        resourceId = R.drawable.ic_user,
                        fieldType = FieldType.EMAIL_OR_USER,
                        validateField = validateUsername(username),
                        isError = !isUsernameValid,
                        supportText = if (!isUsernameValid) stringResource(id = R.string.login_invalid_username)
                        else stringResource(id = R.string.login_help_username)
                    )
                    InformationBox(
                        label = "Password",
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        resourceId = R.drawable.password,
                        fieldType = FieldType.PASSWORD,
                        validateField = validatePassword(password),
                        isError = !isPasswordValid,
                        supportText = if (!isPasswordValid)  stringResource(id = R.string.login_invalid_password)
                        else stringResource(id = R.string.login_help_password)
                    )
                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = stringResource(id = R.string.login_title),
                        enabled = isLoginButtonEnabled,
                        onClick = {
                            isUsernameValid = validateUsername(username)
                            isPasswordValid = validatePassword(password)
                            if (isUsernameValid && isPasswordValid) {
                                onLoginRequested(username, password)
                            }

                        })
                    DivideComponent()

                    VerificationComponent(
                        text = stringResource(id = R.string.login_no_account),
                        textUnderline = "Sign Up",
                        onClick = { onRegisterRequested() }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {
    LoginScreen(
        onLoginRequested = { _, _ -> }
    )
}