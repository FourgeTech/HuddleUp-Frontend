package tech.fourge.huddleup_frontend.Utils

import android.content.Context
import android.content.res.Configuration
import tech.fourge.huddleup_frontend.Models.Settings
import tech.fourge.huddleup_frontend.Models.UserModel
import java.util.Locale

class CurrentUserUtil {
    companion object {
        var currentUser: UserModel = UserModel("", "", "", "", "")
        var currentUserUID: String = ""
        var fcmToken: String = ""
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

        fun updateLocale(context: Context, languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
}

