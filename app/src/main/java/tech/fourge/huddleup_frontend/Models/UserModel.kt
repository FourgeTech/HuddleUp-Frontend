package tech.fourge.huddleup_frontend.Models

// Settings data class
data class Settings(
    var matchAlerts: Boolean = true,
    var practiceAlerts: Boolean = true,
    var chatNotifications: Boolean = true,
    var preferredLanguage: String = "en",
    var theme: String = "light"
) {
    // Method to convert Settings to a Map
    fun toMap(): Map<String, Any> {
        return mapOf(
            "matchAlerts" to matchAlerts,
            "practiceAlerts" to practiceAlerts,
            "chatNotifications" to chatNotifications,
            "preferredLanguage" to preferredLanguage,
            "theme" to theme
        )
    }

    // Method to create Settings from a Map
    companion object {
        fun fromMap(map: Map<String, Any?>): Settings {
            return Settings(
                matchAlerts = map["matchAlerts"] as? Boolean ?: true,
                practiceAlerts = map["practiceAlerts"] as? Boolean ?: true,
                chatNotifications = map["chatNotifications"] as? Boolean ?: true,
                preferredLanguage = map["preferredLanguage"] as? String ?: "en",
                theme = map["theme"] as? String ?: "light"
            )
        }
    }
}

// UserModel data class
data class UserModel(
    var firstname: String,
    var lastname: String,
    var username: String,
    var email: String,
    var role: String,
    var teamIds: List<String> = emptyList(),
    var profilePicUrl: String = "",
    var phoneNumber: String = "",
) {
    // Method to convert UserModel to a Map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "username" to username,
            "email" to email,
            "role" to role,
            "teamIds" to teamIds,
            "profilePicUrl" to profilePicUrl,
            "phoneNumber" to phoneNumber,
        )
    }

    // Method to create UserModel from a Map
    companion object {
        fun fromMap(map: Map<String, Any?>): UserModel {
            return UserModel(
                firstname = map["firstname"] as? String ?: "",
                lastname = map["lastname"] as? String ?: "",
                username = map["username"] as? String ?: "",
                email = map["email"] as? String ?: "",
                role = map["role"] as? String ?: "",
                teamIds = (map["teamIds"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                profilePicUrl = map["profilePicUrl"] as? String ?: "",
                phoneNumber = map["phoneNumber"] as? String ?: "",
            )
        }
    }
}


