package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Helpers.UserHelper.Companion
import tech.fourge.huddleup_frontend.Models.EventModel
import tech.fourge.huddleup_frontend.Utils.FirebaseUtil

class EventHelper {
    private val auth = FirebaseUtil.auth
    private val functions = FirebaseUtil.functions

    init {
        // Use the emulator for local development (comment out for production)
//        functions.useEmulator("10.0.2.2", 5001)
        Log.d("CHEESE", auth.currentUser?.uid.toString())
    }

    suspend fun getEventsByTeamId(teamId: String): List<EventModel>? {
        return try {
            val data = mapOf("teamId" to teamId)
            val result = functions.getHttpsCallable("getEventsByTeamId").call(data).await()

            Log.d("HAM", result.data.toString())
            val resultData = result.data as? Map<*, *>
            val eventsData = resultData?.get("events") as? List<Map<String, Any?>>
            Log.d(TAG, "Events data: $eventsData")

            // Convert the list of maps to a list of EventModel objects
            eventsData?.map { eventMap ->
                EventModel.fromMap(eventMap)
            }


        } catch (e: FirebaseFunctionsException) {
            Log.e(TAG, "FirebaseFunctionsException in getEventsByTeamId: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unknown exception in getEventsByTeamId: ${e.message}", e)
            null
        }
    }

    suspend fun createEvent(event: EventModel): Boolean {
        return try {
            val data = event.toMap()
            functions.getHttpsCallable("addEvent").call(data).await()
            true
        } catch (e: FirebaseFunctionsException) {
            Log.e(TAG, "FirebaseFunctionsException in createEvent: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown exception in createEvent: ${e.message}", e)
            false
        }
    }

    companion object {
        private const val TAG = "EventHelper"
    }
}