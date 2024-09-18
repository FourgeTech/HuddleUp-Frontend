package tech.fourge.huddleup_frontend.Utils

import tech.fourge.huddleup_frontend.Models.Settings
import tech.fourge.huddleup_frontend.Models.UserModel

class CurrentUserUtil {
    companion object {
        var currentUser: UserModel = UserModel("", "", "", "", "")
        var currentUserUID: String? = null
        var currentUserSettings: Settings? = null
    }
}