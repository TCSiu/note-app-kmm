package signIn.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.russhwolf.settings.Settings
import core.presentation.LoadingState
import core.response.Response
import core.theme.SettingKeys
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import signIn.data.validator.LoginValidator
import signIn.domain.model.Login
import signIn.domain.response.LoginResponseFail
import signIn.domain.response.LoginResponseSuccess
import signIn.domain.useCase.LoginUseCase

class SignInScreenModel : ScreenModel, KoinComponent {
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val loginUseCase by inject<LoginUseCase>()
    private val settings by inject<Settings>()

    private val httpClient by inject<HttpClient>()

    var newLogin: Login by mutableStateOf(Login(email = "admin@gmail.com", password = "Abcd1234"))
//    var newLogin: Login by mutableStateOf(Login())

    fun onEvent(event: SignInIntent) {
        when (event) {
            is SignInIntent.OnEmailChange -> {
                updateLogin(email = event.email)
            }

            is SignInIntent.OnPasswordChange -> {
                updateLogin(password = event.password)
            }

            SignInIntent.Login -> {
                login()
            }
        }
    }

    private fun updateLogin(email: String? = null, password: String? = null) {
        newLogin = newLogin.copy(
            email = email ?: newLogin.email,
            password = password ?: newLogin.password
        )
    }

    private fun login() {
        screenModelScope.launch {
            newLogin.let { request ->
                val result = LoginValidator.validateLogin(request)
                val errors = listOfNotNull(
                    result.emailError,
                    result.passwordError,
                )
                if (errors.isEmpty()) {
                    _signInState.update {
                        it.copy(
                            emailError = null,
                            passwordError = null,
                            isSubmit = true,
                            loadingState = LoadingState.IsLoading,
                        )
                    }
                    delay(1000L)
                    val response: Response = loginUseCase.login(newLogin)
                    var isSuccess = false
                    response.let {
                        when (it) {
                            is LoginResponseSuccess -> {
                                _signInState.update { state -> state.copy(loadingState = LoadingState.IsSuccess) }
                                settings.putString(SettingKeys.TOKEN, it.data.token)
                                settings.putInt(SettingKeys.USER_ID, it.data.user.id ?: -1)
                                settings.putString(SettingKeys.USER_NAME, it.data.user.name)
                                settings.putString(SettingKeys.USER_EMAIL, it.data.user.email)
                                isSuccess = true
                            }

                            is LoginResponseFail -> {
                                _signInState.update { state -> state.copy(loadingState = LoadingState.IsFail) }
                                _signInState.update { state ->
                                    state.copy(
                                        loginHasError = it.data
                                    )
                                }
                            }
                        }
                        delay(1000L)
                        _signInState.update { state ->
                            state.copy(
                                isSubmit = false,
                                isRedirect = isSuccess,
                                loadingState = LoadingState.Init
                            )
                        }
                    }
                } else {
                    _signInState.update {
                        it.copy(
                            emailError = result.emailError,
                            passwordError = result.passwordError
                        )
                    }
                }
            }
        }
    }

}