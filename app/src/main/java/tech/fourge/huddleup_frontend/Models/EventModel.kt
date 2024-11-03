package tech.fourge.huddleup_frontend.Models

data class EventModel(
    val eventId: String,
    val teamId: String,
    val eventType: String,
    val date: String,
    val time: String,
    val location: String,
    val createdBy: String,
) {
    // Convert EventModel to a Map
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "eventId" to eventId,
            "teamId" to teamId,
            "eventType" to eventType,
            "date" to date,
            "time" to time,
            "location" to location,
            "createdBy" to createdBy,
        )
    }

    companion object {
        // Create EventModel from a Map
        fun fromMap(map: Map<String, Any?>): EventModel {
            return EventModel(
                eventId = map["eventId"] as? String ?: "",
                teamId = map["teamId"] as? String ?: "",
                eventType = map["eventType"] as? String ?: "",
                date = map["date"] as? String ?: "",
                time = map["time"] as? String ?: "",
                location = map["location"] as? String ?: "",
                createdBy = map["createdBy"] as? String ?: ""
            )
        }
    }
}