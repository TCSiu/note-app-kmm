package signUp.data.validator

import signUp.domain.model.Register

object RegisterValidator {
    fun validateRegister(register: Register): ValidationResult {
        var result = ValidationResult()
        if (register.email.isBlank()) {
            result = result.copy(
                emailError = "The email can\'t be empty"
            )
        }
        if (register.name.isBlank()) {
            result = result.copy(
                nameError = "The name can\'t be empty"
            )
        }
        if (register.password.isBlank()) {
            result = result.copy(
                passwordError = "The password can\'t be empty"
            )
        }
        if (register.passwordConfirmation.isBlank()) {
            result = result.copy(
                passwordConfirmationError = "The password confirmation can\'t be empty"
            )
        }
        val emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        if (!emailRegex.matches(register.email)) {
            result = result.copy(emailError = "This is not a valid email")
        }
        val nameRegex = Regex("^[a-zA-Z0-9]{4,10}\$")
        if (!nameRegex.matches(register.name)) {
            result = result.copy(nameError = "This is not a valid name")
        }
        if (register.password != register.passwordConfirmation) {
            result = result.copy(passwordError = "Password Confirmation is not the same!")
        }
        return result
    }

    data class ValidationResult(
        val emailError: String? = null,
        val nameError: String? = null,
        val passwordError: String? = null,
        val passwordConfirmationError: String? = null,
    )
}