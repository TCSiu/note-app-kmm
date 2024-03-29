package signUp.presentation

import signUp.domain.response.RegisterResponseFailData

data class SignUpState(
    val emailError: String? = null,
    val nameError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmationError: String? = null,
    val registerHasError: RegisterResponseFailData? = null,
    val isSubmit: Boolean = false,
    val isRedirect: Boolean = false,
)