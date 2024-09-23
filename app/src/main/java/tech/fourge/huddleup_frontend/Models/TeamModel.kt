package tech.fourge.huddleup_frontend.Models

data class Team(
    val teamId: String = "",
    val teamName: String = "",
    val teamCode: String = "",
    val location: String = "",
    val league: String = "",
    val createdBy: String = "",
    val members: Map<String, String> = emptyMap(),
    val managers: List<String> = emptyList(),
    val players: List<String> = emptyList(),
    val events: List<String> = emptyList()
) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "teamId" to teamId,
            "teamName" to teamName,
            "teamCode" to teamCode,
            "location" to location,
            "league" to league,
            "createdBy" to createdBy,
            "members" to members,
            "managers" to managers,
            "players" to players,
            "events" to events
        )
    }


    // Method to create UserModel from a Map
    companion object {
        fun fromMap(map: Map<String, Any?>): Team {
            return Team(
                teamId = map["teamId"] as String,
                teamName = map["teamName"] as String,
                teamCode = map["teamCode"] as String,
                location = map["location"] as String,
                league = map["league"] as String,
                createdBy = map["createdBy"] as String,
                members = map["members"] as Map<String, String>,
                managers = map["managers"] as List<String>,
                players = map["players"] as List<String>,
                events = map["events"] as List<String>
            )
        }
    }
}

