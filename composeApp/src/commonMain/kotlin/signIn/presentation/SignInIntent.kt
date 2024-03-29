package signIn.presentation

sealed interface SignInIntent {
    class OnEmailChange(val email: String) : SignInIntent
    class OnPasswordChange(val password: String) : SignInIntent
    data object Login : SignInIntent
}