package ui.signIn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.presentation.CustomDivider
import core.presentation.LoadingBox
import core.presentation.TopAppBarWithBackButton
import core.presentation.input.CustomTextField
import core.theme.DefaultButtonWithBorderPrimaryTheme
import core.theme.SPACER_HEIGHT
import core.theme.STANDARD_LAYOUT_PADDING
import core.theme.Shapes
import newkmmnoteapp.composeapp.generated.resources.Res
import newkmmnoteapp.composeapp.generated.resources.email
import newkmmnoteapp.composeapp.generated.resources.login
import newkmmnoteapp.composeapp.generated.resources.no_account
import newkmmnoteapp.composeapp.generated.resources.or
import newkmmnoteapp.composeapp.generated.resources.password
import newkmmnoteapp.composeapp.generated.resources.register
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import signIn.domain.model.Login
import signIn.presentation.SignInIntent
import signIn.presentation.SignInScreenModel
import ui.home.HomeScreen
import ui.signUp.SignUpScreen

class SignInScreen : Screen, KoinComponent {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val signInScreenModel = getScreenModel<SignInScreenModel>()
        val signInState by signInScreenModel.signInState.collectAsState()
        val onEvent = signInScreenModel::onEvent
        val newLogin: Login = signInScreenModel.newLogin

        LaunchedEffect(signInState) {
            if (signInState.isRedirect) {
                navigator.replace(HomeScreen())
            }
        }

        Scaffold(
            topBar = {
                TopAppBarWithBackButton(title = stringResource(resource = Res.string.login))
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(STANDARD_LAYOUT_PADDING)
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                CustomTextField(
                    value = newLogin.email,
                    onValueChanged = { value -> onEvent(SignInIntent.OnEmailChange(value)) },
                    error = signInState.emailError,
                    placeholder = stringResource(resource = Res.string.email),
                )
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                CustomTextField(
                    value = newLogin.password,
                    onValueChanged = { value -> onEvent(SignInIntent.OnPasswordChange(value)) },
                    error = signInState.passwordError,
                    placeholder = stringResource(resource = Res.string.password),
                    isPasswordTextField = true,
                )
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                ElevatedButton(
                    onClick = {
                        onEvent(SignInIntent.Login)
                    },
                    shape = Shapes.medium,
                    colors = DefaultButtonWithBorderPrimaryTheme(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !signInState.isSubmit
                ) {
                    Text(text = stringResource(resource = Res.string.login))
                }
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                CustomDivider(display = stringResource(resource = Res.string.or))
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(resource = Res.string.no_account) + " ")
                    ClickableText(
                        text = AnnotatedString(text = stringResource(resource = Res.string.register)),
                        onClick = {
                            navigator.replace(SignUpScreen())
                        },
                        style = TextStyle(color = Color.Blue)
                    )
                }
            }
        }

        LoadingBox(
            isShow = signInState.isSubmit,
            loadingState = signInState.loadingState,
        )
    }
}