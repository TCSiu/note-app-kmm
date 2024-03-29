package signUp.presentation

sealed interface SignUpIntent {
    class OnEmailChange(val email: String) : SignUpIntent
    class OnNameChange(val name: String) : SignUpIntent
    class OnPasswordChange(val password: String) : SignUpIntent
    class OnPasswordConfirmationChange(val passwordConfirmation: String) : SignUpIntent
    data object Register : SignUpIntent
}