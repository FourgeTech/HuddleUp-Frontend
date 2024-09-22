package tech.fourge.huddleup_frontend.Utils

class ValidationUtils {

    // Validates email format
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Validates password length and complexity (example: minimum 8 characters, with letters and numbers)
    fun isValidPassword(password: String): Boolean {
        return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isLetter() }
    }

    // Validates if the input is not empty or blank
    fun isNotEmpty(input: String): Boolean {
        return input.trim().isNotEmpty()
    }

    // Validates if phone number is valid (can adjust pattern depending on country)
    fun isValidPhoneNumber(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    // Validates if two strings match (like password and confirm password)
    fun doStringsMatch(str1: String, str2: String): Boolean {
        return str1 == str2
    }

    // Validates if an input is a valid number
    fun isValidNumber(input: String): Boolean {
        return input.toDoubleOrNull() != null
    }

    // Validates if an input is a valid URL
    fun isValidUrl(url: String): Boolean {
        return android.util.Patterns.WEB_URL.matcher(url).matches()
    }

    // Example validation for a custom field (adjust according to your needs)
    fun isValidCustomField(input: String, minLength: Int, maxLength: Int): Boolean {
        return input.length in minLength..maxLength
    }

    // Validate name (for example, no numbers or special characters)
    fun isValidName(name: String): Boolean {
        return name.all { it.isLetter() || it.isWhitespace() }
    }
    // Validate username (example: minimum 3 characters, no special characters)
    fun isValidUsername(username: String): Boolean {
        return username.length >= 3 && username.all { it.isLetterOrDigit() }
    }
}