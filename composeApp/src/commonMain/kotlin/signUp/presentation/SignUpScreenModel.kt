package signUp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.russhwolf.settings.Settings
import core.presentation.LoadingState
import core.response.Response
import core.theme.SettingKeys
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import signUp.data.validator.RegisterValidator
import signUp.domain.model.Register
import signUp.domain.response.RegisterResponseFail
import signUp.domain.response.RegisterResponseSuccess
import signUp.domain.useCase.RegisterUseCase

class SignUpScreenModel : ScreenModel, KoinComponent {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Init)
    val loadingState = _loadingState.asStateFlow()

    private val registerUseCase by inject<RegisterUseCase>()
    private val settings by inject<Settings>()

    var newRegister: Register by mutableStateOf(
        Register(
            email = "admin2@gmail.com",
            name = "admin2",
            password = "Abcd1234",
            passwordConfirmation = "Abcd1234"
        )
    )

    fun onEvent(event: SignUpIntent) {
        when (event) {
            is SignUpIntent.OnEmailChange -> {
                updateRegister(event.email)
            }

            is SignUpIntent.OnNameChange -> {
                updateRegister(event.name)
            }

            is SignUpIntent.OnPasswordChange -> {
                updateRegister(event.password)
            }

            is SignUpIntent.OnPasswordConfirmationChange -> {
                updateRegister(event.passwordConfirmation)
            }

            is SignUpIntent.Register -> {
                register()
            }
        }
    }

    private fun updateRegister(
        email: String? = null,
        name: String? = null,
        password: String? = null,
        passwordConfirmation: String? = null,
    ) {
        newRegister = newRegister.copy(
            email = email ?: newRegister.email,
            name = name ?: newRegister.name,
            password = password ?: newRegister.password,
            passwordConfirmation = passwordConfirmation ?: newRegister.passwordConfirmation
        )
    }

    private fun register() {
        screenModelScope.launch {
            newRegister.let { request ->
                val result = RegisterValidator.validateRegister(request)
                val errors = listOfNotNull(
                    result.emailError,
                    result.nameError,
                    result.passwordError,
                    result.passwordConfirmationError
                )
                if (errors.isEmpty()) {
                    _loadingState.update { LoadingState.IsLoading }
                    _signUpState.update {
                        it.copy(
                            emailError = null,
                            nameError = null,
                            passwordError = null,
                            passwordConfirmationError = null,
                            isSubmit = true,
                        )
                    }
                    delay(1000L)
                    val response: Response = registerUseCase.register(newRegister)
                    var isSuccess = false
                    response.let {
                        when (it) {
                            is RegisterResponseSuccess -> {
                                _loadingState.update { LoadingState.IsSuccess }
                                settings.putString(SettingKeys.TOKEN, it.data.token)
                                settings.putInt(SettingKeys.USER_ID, it.data.user.id ?: -1)
                                settings.putString(SettingKeys.USER_NAME, it.data.user.name)
                                settings.putString(SettingKeys.USER_EMAIL, it.data.user.email)
                                isSuccess = true
                            }

                            is RegisterResponseFail -> {
                                _loadingState.update { LoadingState.IsFail }
                                _signUpState.update { state ->
                                    state.copy(
                                        registerHasError = it.data
                                    )
                                }
                            }
                        }
                        delay(1000L)
                        _signUpState.update { state -> state.copy(isSubmit = false) }
                        _signUpState.update { state -> state.copy(isRedirect = isSuccess) }
                    }
                } else {
                    _signUpState.update {
                        it.copy(
                            emailError = result.emailError,
                            nameError = result.nameError,
                            passwordError = result.passwordError,
                            passwordConfirmationError = result.passwordConfirmationError,
                        )
                    }
                }
            }
        }
    }

}