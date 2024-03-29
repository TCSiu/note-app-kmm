package signIn.data.validator

import signIn.domain.model.Login

object LoginValidator {
    fun validateLogin(login: Login): ValidationResult {
        var result = ValidationResult()
        if (login.email.isBlank()) {
            result = result.copy(
                emailError = "The email can\'t be empty"
            )
        }
        if (login.password.isBlank()) {
            result = result.copy(
                passwordError = "The password can\'t be empty"
            )
        }
        val emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        if (!emailRegex.matches(login.email)) {
            result = result.copy(emailError = "This is not a valid email")
        }
        return result
    }

    data class ValidationResult(
        val emailError: String? = null,
        val passwordError: String? = null,
    )
}