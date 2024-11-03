package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Models.Team
import tech.fourge.huddleup_frontend.Models.UserModel
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Utils.FirebaseUtil

class TeamHelper {
    private val auth = FirebaseUtil.auth
    private val functions = FirebaseUtil.functions

    // On Class Creation
    init {
        // Use the emulator for local development (comment out for production)
//       functions.useEmulator("10.0.2.2", 5001)
////        functions.useEmulator("10.0.2.2", 5001)
//        auth.useEmulator("10.0.2.2", 9099)
        Log.d(TAG, auth.currentUser.toString())
    }

    // Register a new team
    suspend fun registerTeam(
        teamName: String,
        location: String,
        league: String
    ): String {
        val currentUserId = CurrentUserUtil.currentUserUID
        val teamId = generateTeamId() // Function to generate a team ID
        val teamCode = generateTeamCode() // Function to generate a unique team code

        val team = Team(
            teamId = teamId,
            teamName = teamName,
            teamCode = teamCode,
            location = location,
            league = league,
            createdBy = currentUserId,
            members = mapOf(currentUserId to "Manager"),
            managers = listOf(currentUserId),
            players = emptyList(),
            events = emptyList()
        )

        return try {
            val data = team.toMap()
            val result = functions.getHttpsCallable("registerTeam").call(data).await()
            val resultData = result.data as? Map<*, *>
            val status = resultData?.get("status") as? String
            val message = resultData?.get("message") as? String
            val teamCode = resultData?.get("teamCode") as? String

            val updateData = mapOf("role" to "Manager")
            UserHelper().updateUserRole(currentUserId, updateData)

            if (status == "success") {
                teamCode ?: "unknown_error"

            } else {
                message ?: "unknown_error"
            }
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Team registration failed", e)
            "functions_error"
        } catch (e: Exception) {
            Log.w(TAG, "Team registration failed", e)
            "unknown_error"
        }
    }

    // Update team information
    suspend fun updateTeam(teamId: String, updatedTeam: Team): Boolean {
        return try {
            val teamData = updatedTeam.toMap()

            // Call the Firebase Cloud Function to update the team
            val result = functions.getHttpsCallable("updateTeam").call(mapOf("teamId" to teamId, "teamData" to teamData)).await()

            val success = result.data as? Boolean
            if (success == true) {
                Log.d(TAG, "Team successfully updated")
                true
            } else {
                Log.d(TAG, "Team update failed")
                false
            }
        } catch (e: Exception) {
            Log.w(TAG, "Team update failed", e)
            false
        }
    }

    suspend fun updateTeamPlayers(teamId: String, players: Map<String, Int>): Boolean {
        return try {
            // Prepare the data to update only the players
            val updateData = mapOf("players" to players)

            // Call the Firebase Cloud Function to update the team players
            val result = functions.getHttpsCallable("updateTeam").call(mapOf("teamId" to teamId, "teamData" to updateData)).await()

            val success = result.data as? Boolean
            success == true
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update team players", e)
            false
        }
    }

    // Fetch team details
    suspend fun getTeam(teamId: String): Team? {
        return try {
            val result = functions.getHttpsCallable("getTeam").call(mapOf("teamId" to teamId)).await()
            Log.d("Result", result.data.toString())
            val data = result.data as? Map<String, Any>
            Log.d("Data", data.toString())
            data?.let {
                Team.fromMap(it)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get team", e)
            null
        }
    }

    // Helper function to generate team ID
    private fun generateTeamId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid + "_" + System.currentTimeMillis().toString()
    }

    // Helper function to generate team code
    private fun generateTeamCode(): String {
        val charset = ('A'..'Z') + ('0'..'9')
        return List(6) { charset.random() }.joinToString("")
    }

    // Join a team by the generated team code
    suspend fun joinTeamByCode(teamCode: String): String {
        val currentUserId = auth.currentUser?.uid ?: return "not_authenticated"

        return try {
            // Call the Firebase Function to join the team using the team code
            val result = functions.getHttpsCallable("joinTeamByCode").call(mapOf(
                "teamCode" to teamCode,
                "userId" to currentUserId
            )).await()


            val updateData = mapOf("role" to "Player")
            UserHelper().updateUserRole(currentUserId, updateData)

            val resultData = result.data as? Map<*, *>
            val status = resultData?.get("status") as? String
            val message = resultData?.get("message") as? String

            if (status == "success") {
                "success"
            } else {
                message ?: "unknown_error"
            }
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Failed to join team", e)
            return "functions_error"
        } catch (e: Exception) {
            Log.w(TAG, "Failed to join team", e)
            return "unknown_error"
        }
    }

    // Fetch team details and return a list of players' full names
    suspend fun getTeamPlayerNames(teamId: String): List<String> {
        return try {
            val result = functions.getHttpsCallable("getTeam").call(mapOf("teamId" to teamId)).await()

            val data = result.data as? Map<String, Any>
            val members = data?.get("members") as? Map<String, String> ?: emptyMap()

            // Filter members to get only players
            val playerIds = members.filter { it.value == "Player" }.keys

            // Fetch user details for each player and get their full names
            val playerNames = playerIds.mapNotNull { playerId ->
                val user = getUser(playerId)
                user?.let { "${it.firstname} ${it.lastname}" }
            }

            // Sort player names alphabetically by last name
            playerNames.sortedBy { it.split(" ").last() }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get team", e)
            emptyList()
        }
    }

    suspend fun getTeamPlayers(teamId: String): Map<String, Int> {
        return try {
            val result = functions.getHttpsCallable("getTeam").call(mapOf("teamId" to teamId)).await()
            val data = result.data as? Map<String, Any>
            val players = data?.get("players") as? Map<String, Int> ?: emptyMap()
            players
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get team players", e)
            emptyMap()
        }
    }

    private suspend fun getUser(userId: String): UserModel? {
        return try {
            val result = functions.getHttpsCallable("getUser").call(mapOf("uid" to userId)).await()
            val data = result.data as? Map<String, Any>
            data?.let { UserModel.fromMap(it) }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get user", e)
            null
        }
    }


    companion object {
        private const val TAG = "TeamHelper"
    }




}