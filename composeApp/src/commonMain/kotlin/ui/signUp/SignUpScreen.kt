package ui.signUp

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
import newkmmnoteapp.composeapp.generated.resources.have_account
import newkmmnoteapp.composeapp.generated.resources.login
import newkmmnoteapp.composeapp.generated.resources.name
import newkmmnoteapp.composeapp.generated.resources.or
import newkmmnoteapp.composeapp.generated.resources.password
import newkmmnoteapp.composeapp.generated.resources.password_confirmation
import newkmmnoteapp.composeapp.generated.resources.register
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import signUp.domain.model.Register
import signUp.presentation.SignUpIntent
import signUp.presentation.SignUpScreenModel
import ui.home.HomeScreen
import ui.signIn.SignInScreen

class SignUpScreen : Screen, KoinComponent {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val signUpScreenModel = getScreenModel<SignUpScreenModel>()
        val signUpState by signUpScreenModel.signUpState.collectAsState()
        val loadingState by signUpScreenModel.loadingState.collectAsState()
        val newRegister: Register = signUpScreenModel.newRegister
        val onEvent = signUpScreenModel::onEvent

        LaunchedEffect(signUpState) {
            if (signUpState.isRedirect) {
                navigator.replace(HomeScreen())
            }
        }

        Scaffold(
            topBar = {
                TopAppBarWithBackButton(title = stringResource(resource = Res.string.register))
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = STANDARD_LAYOUT_PADDING)
                    .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextField(
                    value = newRegister.email,
                    onValueChanged = { value -> onEvent(SignUpIntent.OnEmailChange(value)) },
                    error = signUpState.emailError,
                    placeholder = stringResource(resource = Res.string.email),
                )
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                CustomTextField(
                    value = newRegister.name,
                    onValueChanged = { value -> onEvent(SignUpIntent.OnNameChange(value)) },
                    error = signUpState.nameError,
                    placeholder = stringResource(resource = Res.string.name),
                )
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                CustomTextField(
                    value = newRegister.password,
                    onValueChanged = { value -> onEvent(SignUpIntent.OnPasswordChange(value)) },
                    error = signUpState.passwordError,
                    placeholder = stringResource(resource = Res.string.password),
                )
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                CustomTextField(
                    value = newRegister.password,
                    onValueChanged = { value -> onEvent(SignUpIntent.OnPasswordChange(value)) },
                    error = signUpState.passwordConfirmationError,
                    placeholder = stringResource(resource = Res.string.password_confirmation),
                )
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                ElevatedButton(
                    onClick = {
                        onEvent(SignUpIntent.Register)
                    },
                    shape = Shapes.medium,
                    colors = DefaultButtonWithBorderPrimaryTheme(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(resource = Res.string.register))
                }
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                CustomDivider(display = stringResource(resource = Res.string.or))
                Spacer(modifier = Modifier.height(SPACER_HEIGHT))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(resource = Res.string.have_account) + " ")
                    ClickableText(
                        text = AnnotatedString(text = stringResource(resource = Res.string.login)),
                        onClick = {
                            navigator.replace(SignInScreen())
                        },
                        style = TextStyle(color = Color.Blue)
                    )
                }
            }
        }

        LoadingBox(
            isShow = signUpState.isSubmit,
            loadingState = loadingState,
        )
    }
}