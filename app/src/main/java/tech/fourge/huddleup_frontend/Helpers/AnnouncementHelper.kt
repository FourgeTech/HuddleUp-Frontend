package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Models.AnnouncementModel

class AnnouncementHelper {

    // Firebase Functions instance
    private val functions = FirebaseFunctions.getInstance()
//
//    init {
//        // Use the emulator for local development (comment out for production)
//       functions.useEmulator("10.0.2.2", 5001)
//        functions.useEmulator("10.0.2.2", 5001)
//    }

    // Get announcements by teamId
    suspend fun getAnnouncementsByTeamId(teamId: String): List<AnnouncementModel>? {
        return try {
            val data = mapOf("teamId" to teamId)
            val result = functions.getHttpsCallable("getAnnouncementsByTeamId").call(data).await()
            val announcementsData = result.data as? List<Map<String, Any?>>

            // Convert the list of maps to a list of AnnouncementModel objects
            announcementsData?.map { announcementMap ->
                AnnouncementModel.fromMap(announcementMap)
            }
        } catch (e: FirebaseFunctionsException) {
            Log.e(TAG, "FirebaseFunctionsException in getAnnouncementsByTeamId: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unknown exception in getAnnouncementsByTeamId: ${e.message}", e)
            null
        }
    }

    // Function to create a new announcement
    suspend fun createAnnouncement(announcement: AnnouncementModel): Boolean {
        return try {
            val announcementMap = announcement.toMap()
            val result = functions.getHttpsCallable("createAnnouncement").call(announcementMap).await()
            val resultMap = result.data as? Map<String, Any?>
            val status = resultMap?.get("status") as? String
            val success = status == "success"

            if (success) {
                Log.d(TAG, "Announcement created successfully with ID: ${resultMap?.get("announcementId")}")
            } else {
                Log.e(TAG, "Failed to create announcement: ${resultMap?.get("message")}")
            }

            success
        } catch (e: FirebaseFunctionsException) {
            Log.e(TAG, "FirebaseFunctionsException in createAnnouncement: ${e.message}", e)
            Log.e(TAG, "Details: ${e.details}")
            Log.e(TAG, "Code: ${e.code}")
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown exception in createAnnouncement: ${e.message}", e)
            false
        }
    }

    // Function to update an existing announcement
    suspend fun updateAnnouncement(announcementId: String, updatedData: AnnouncementModel): Boolean {
        return try {
            val announcementMap = updatedData.toMap()
            val data = mapOf("announcementId" to announcementId, "announcementData" to announcementMap)
            val result = functions.getHttpsCallable("updateAnnouncement").call(data).await()
            result.data as? Boolean ?: false
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Failed to update announcement: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.w(TAG, "Unknown error occurred: ${e.message}", e)
            false
        }
    }

    // Function to delete an announcement
    suspend fun deleteAnnouncement(announcementId: String): Boolean {
        return try {
            val data = mapOf("announcementId" to announcementId)
            val result = functions.getHttpsCallable("deleteAnnouncement").call(data).await()
            result.data as? Boolean ?: false
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Failed to delete announcement: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.w(TAG, "Unknown error occurred: ${e.message}", e)
            false
        }
    }

    companion object {
        private const val TAG = "AnnouncementHelper"
    }
}
