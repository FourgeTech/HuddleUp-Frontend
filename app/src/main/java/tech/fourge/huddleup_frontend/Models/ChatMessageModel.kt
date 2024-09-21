package tech.fourge.huddleup_frontend.Models

import android.util.Log
import com.google.firebase.Timestamp
import java.util.Date

// Message data class
data class Message(
    var senderId: String = "",
    var sender: String = "",
    var content: String = "",
    var timestamp: String? = null,
    var teamId: String = ""
) {
    // Method to convert Message to a Map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "senderId" to senderId,
            "sender" to sender,
            "content" to content,
            "teamId" to teamId
        )
    }

    // Method to create Message from a Map
    companion object {
        fun fromMap(map: Map<String, Any?>): Message {
            return Message(
                senderId = map["senderId"] as? String ?: "",
                sender = map["sender"] as? String ?: "",
                content = map["content"] as? String ?: "",
                timestamp = map["timestamp"] as? String,
                teamId = map["teamId"] as? String ?: ""
            )
        }

    }
}