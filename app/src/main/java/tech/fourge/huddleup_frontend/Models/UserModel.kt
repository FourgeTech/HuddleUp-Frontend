package tech.fourge.huddleup_frontend.Models

data class UserModel(
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val role: String,
    val teamIds: List<String> = emptyList(), // Default to an empty list if not provided
    val profilePicUrl: String = "", // Default to an empty string if not provided
    val phoneNumber: String = "", // Default to an empty string if not provided
    val settings: Settings = Settings() // Default to a new Settings instance if not provided
)

data class Settings(
    val matchAlerts: Boolean = true,
    val practiceAlerts: Boolean = true,
    val chatNotifications: Boolean = true,
    val preferredLanguage: String = "en",
    val theme: String = "light"
)
