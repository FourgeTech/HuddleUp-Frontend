package tech.fourge.huddleup_frontend.Models

data class AnnouncementModel(
    val name: String,
    val time: String,
    val announcementText: String,
    val views: String
)

    var announcementId: String,
    var teamId: String,
    var title: String,
    var message: String,
    var createdBy: String,
    var createdAt: String,
    var viewCount: Int
) {
    // Convert AnnouncementModel to a Map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "announcementId" to announcementId,
            "teamId" to teamId,
            "title" to title,
            "message" to message,
            "createdBy" to createdBy,
            "createdAt" to createdAt,
            "viewCount" to viewCount
        )
    }

    companion object {
        // Create AnnouncementModel from a Map
        fun fromMap(map: Map<String, Any?>): AnnouncementModel {
            return AnnouncementModel(
                announcementId = map["announcementId"] as? String ?: "",
                teamId = map["teamId"] as? String ?: "",
                title = map["title"] as? String ?: "",
                message = map["message"] as? String ?: "",
                createdBy = map["createdBy"] as? String ?: "",
                createdAt = map["createdAt"] as? String ?: "",
                viewCount = map["viewCount"] as? Int ?: 0
            )
        }
    }
}

