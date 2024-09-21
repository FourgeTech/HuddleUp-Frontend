package tech.fourge.huddleup_frontend.Models

import java.sql.Date

data class ChatGroupModel(
    var chatId: String = "",
    var chatName: String,
    var type: String,
    var description: String = "",
    var members: List<String> = emptyList(),
    var teamId: String? = null,
    var createdAt: String? = null
) {
    // Method to convert ChatModel to a Map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "chatName" to chatName,
            "type" to type,
            "description" to description,
            "members" to members,
            "teamId" to teamId,
            "createdAt" to createdAt
        )
    }

    // Method to create ChatModel from a Map
    companion object {
        fun fromMap(map: Map<String, Any?>): ChatGroupModel {
            return ChatGroupModel(
                chatId = map["chatId"] as? String ?: "",
                chatName = map["chatName"] as? String ?: "",
                type = map["type"] as? String ?: "",
                description = map["description"] as? String ?: "",
                members = (map["members"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                teamId = map["teamId"] as? String,
                createdAt = map["createdAt"] as? String
            )
        }
    }
}
