package tech.fourge.huddleup_frontend.Utils

import tech.fourge.huddleup_frontend.Models.Settings
import tech.fourge.huddleup_frontend.Models.UserModel

class CurrentUserUtil {
    companion object {
        var currentUser: UserModel = UserModel("", "", "", "", "")
        var currentUserUID: String = ""
        var currentUserSettings: Settings = Settings(
            matchAlerts = true,
            practiceAlerts = true,
            chatNotifications = true,
            preferredLanguage = "en",
            theme = "light"
        )

        fun updateSettings(newSettings: Settings){
            currentUserSettings = newSettings
        }
    }
}
