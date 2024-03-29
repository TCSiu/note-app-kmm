package signIn.presentation

import core.presentation.LoadingState
import signIn.domain.response.LoginResponseFailData

data class SignInState(
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginHasError: LoginResponseFailData? = null,
    val isSubmit: Boolean = false,
    val loadingState: LoadingState = LoadingState.Init,
    val isRedirect: Boolean = false,
)
