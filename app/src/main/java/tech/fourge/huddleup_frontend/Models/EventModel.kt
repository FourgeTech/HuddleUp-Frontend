package tech.fourge.huddleup_frontend.Models

data class EventModel(
    val eventId: String,
    val teamId: String,
    val eventType: String,
    val date: String,
    val time: String,
    val location: String,
    val createdBy: String,
    val createdAt: String
)