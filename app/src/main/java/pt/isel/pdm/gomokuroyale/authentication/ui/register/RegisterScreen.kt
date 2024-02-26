package pt.isel.pdm.gomokuroyale.authentication.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.authentication.domain.validateConfirmationPassword
import pt.isel.pdm.gomokuroyale.authentication.domain.validateEmail
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

const val RegisterScreenTestTag = "RegisterScreenTestTag"
const val RegisterCheckBoxTestTag = "RegisterButtonTestTag"
private val paddingHead = 30.dp
private val errorAcceptTermsSize = 12.sp

@Composable
fun RegisterScreen(
    onBackRequested: () -> Unit = { },
    onLoginActivity: () -> Unit = { },
    onRegisterRequested: (username: String, email: String, password: String) -> Unit
) {
    GomokuRoyaleTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(RegisterScreenTestTag),
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
                    var email by rememberSaveable { mutableStateOf("") }
                    var password by rememberSaveable { mutableStateOf("") }
                    var passwordConfirmation by rememberSaveable { mutableStateOf("") }
                    var acceptTerms by rememberSaveable { mutableStateOf(false) }
                    var isUsernameValid by rememberSaveable { mutableStateOf(false) }
                    var isEmailValid by rememberSaveable { mutableStateOf(false) }
                    var isPasswordValid by rememberSaveable { mutableStateOf(false) }
                    var isPasswordConfirmationValid by rememberSaveable { mutableStateOf(false) }
                    var isTermsValid by rememberSaveable { mutableStateOf(false) }
                    var isButtonClicked by rememberSaveable { mutableStateOf(false) }

                    TextComponent(R.string.register_title)
                    InformationBox(
                        label = stringResource(id = R.string.register_username), value = username,
                        onValueChange = {
                            username = it
                        },
                        resourceId = R.drawable.ic_user,
                        fieldType = FieldType.EMAIL_OR_USER,
                        validateField = validateUsername(username),
                        isError = !isUsernameValid && isButtonClicked,
                        supportText =
                        if (!isUsernameValid && isButtonClicked)
                            stringResource(id = R.string.register_invalid_username)
                        else stringResource(R.string.register_help_username)
                    )
                    InformationBox(
                        label = stringResource(id = R.string.register_email), value = email,
                        onValueChange = {
                            email = it
                        },
                        resourceId = R.drawable.email,
                        fieldType = FieldType.EMAIL_OR_USER,
                        validateField = validateEmail(email),
                        isError = !isEmailValid && isButtonClicked,
                        supportText = if (!isEmailValid && isButtonClicked)
                            stringResource(id = R.string.register_invalid_email)
                        else stringResource(R.string.register_help_email)
                    )
                    InformationBox(
                        label = stringResource(id = R.string.register_password),
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        resourceId = R.drawable.password,
                        fieldType = FieldType.PASSWORD,
                        validateField = validatePassword(password),
                        isError = !isPasswordValid && isButtonClicked,
                        supportText = if (!isPasswordValid && isButtonClicked)
                            stringResource(id = R.string.register_invalid_password)
                        else stringResource(R.string.register_help_password)
                    )
                    InformationBox(
                        label = stringResource(id = R.string.register_confirm_password),
                        value = passwordConfirmation,
                        onValueChange = {
                            passwordConfirmation = it
                        },
                        resourceId = R.drawable.password,
                        fieldType = FieldType.PASSWORD,
                        validateField = validateConfirmationPassword(
                            password,
                            passwordConfirmation
                        ),
                        isError = !isPasswordConfirmationValid && isButtonClicked,
                        supportText = if (!isPasswordConfirmationValid && isButtonClicked)
                            stringResource(id = R.string.register_invalid_password)
                        else stringResource(R.string.register_help_password)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Checkbox(modifier = Modifier.testTag(RegisterCheckBoxTestTag),
                            checked = acceptTerms,
                            onCheckedChange =
                            {
                                acceptTerms = it
                            })

                        Text(text = "Accept Terms ")

                        if (!acceptTerms && isButtonClicked) {
                            Text(
                                text = stringResource(id = R.string.register_invalid_terms),
                                fontSize = errorAcceptTermsSize,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    ButtonComponent(
                        iconResourceId = R.drawable.ic_enter,
                        text = stringResource(id = R.string.register_title),
                        onClick = {
                            isButtonClicked = true
                            isUsernameValid = validateUsername(username)
                            isEmailValid = validateEmail(email)
                            isPasswordValid = validatePassword(password)
                            isPasswordConfirmationValid = validateConfirmationPassword(
                                password,
                                passwordConfirmation
                            )
                            isTermsValid = acceptTerms

                            if (isUsernameValid && isEmailValid && isPasswordValid && isPasswordConfirmationValid && isTermsValid)
                                onRegisterRequested(username, email, password)
                        })
                    DivideComponent()
                    VerificationComponent(
                        text = stringResource(id = R.string.register_already_registered),
                        textUnderline = stringResource(id = R.string.register_login_question),
                    ) { onLoginActivity() }
                }

            }

        }


    }
}


@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {

    RegisterScreen(
        onBackRequested = { },
        onLoginActivity = { },
        onRegisterRequested = { _, _, _ -> })
}
