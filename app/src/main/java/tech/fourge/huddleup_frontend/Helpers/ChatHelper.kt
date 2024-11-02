package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Models.ChatGroupModel
import tech.fourge.huddleup_frontend.Models.Message

class ChatHelper {
    private val functions = Firebase.functions

    // On Class Creation
//    init {
//        // Use the emulator for local development (comment out for production)
//        functions.useEmulator("10.0.2.2", 5001)
//    }

    // Function to update settings
    suspend fun newMessage(chatId: String, message: Message): Boolean {
        val addMessageToChat = functions.getHttpsCallable("addMessageToChat")

        return try {
            val messageMap = message.toMap()
            val result = addMessageToChat.call(mapOf("chatId" to chatId, "message" to messageMap )).await()
            val success = result.data as? Boolean
            success == true
        } catch (e: Exception) {
            Log.d(TAG, "Failed to update settings: ${e.message}")
            false
        }
    }

    // Function to update settings
    suspend fun newChat(chatName: String, chatDescription: String, teamId: String, type: String): String {
        val createChat = functions.getHttpsCallable("createChat")

        return try {
            val result = createChat.call(mapOf("chatName" to chatName, "type" to type, "description" to chatDescription)).await()
            val resultData = result.data as Map<String, Any>
            val chatId = resultData["chatId"] as? String
            chatId ?: "error"
        } catch (e: Exception) {
            Log.d(TAG, "Failed to update settings: ${e.message}")
            "error"
        }
    }

    suspend fun loadMessages(chatId: String): List<Message> {
        val data = hashMapOf(
            "chatId" to chatId
        )
        var messages = emptyList<Message>()
        val function = functions.getHttpsCallable("loadMessages")
        try {
            val result = function.call(data).await()
            val dataMap = result.data as? Map<String, Any>
            // Then, access the 'messages' field and cast it to a List<Map<String, Any>>
            val messagesList = dataMap?.get("messages") as? List<Map<String, Any>>
            if (messagesList != null) {

                // Map the result to a list of ChatMessage objects
                messages = messagesList.map { messageMap ->
                    Message.fromMap(messageMap)
                }
            }
        }
        catch ( e: Exception) {
            Log.d(TAG, "Failed to load messages: ${e.message}")
        }
        Log.d(TAG, "Messages loaded: ${messages}")
        return messages
    }

    suspend fun addChatIdToUsers(userIds: List<String>?, chatId: String): Result<String> {
        val data = hashMapOf(
            "userIds" to userIds,
            "chatId" to chatId
        )
        return try {
            // Call the function
            val result = functions
                .getHttpsCallable("addChatIdToUsers")
                .call(data)
                .await() // Suspends until the result is available

            // Handle the result (you can modify this based on your needs)
            Result.success(result.data as String)
        } catch (e: Exception) {
            // Handle any exceptions
            Result.failure(e)
        }
    }

    suspend fun getUserChats(userId: String): List<ChatGroupModel> {
        val data = hashMapOf(
            "userId" to userId
        )
        var chats = emptyList<ChatGroupModel>()
        val function = functions.getHttpsCallable("getChatDetails")
        try {
            val result = function.call(data).await()
            val dataMap = result.data as? Map<String, Any>
            Log.d(TAG, "dataMap: ${dataMap}")

            val chatsList = dataMap?.get("messages") as? List<Map<String, Any>>
            Log.d(TAG, "chatList: ${chatsList}")
            if (chatsList != null) {

                // Map the result to a list of ChatMessage objects
                chats = chatsList.map { chatMap ->
                    ChatGroupModel.fromMap(chatMap)
                }
            }
        }
        catch ( e: Exception) {
            Log.d(TAG, "Failed to load messages: ${e.message}")
        }
        Log.d(TAG, "Chats loaded: ${chats}")
        return chats
    }

    companion object {
        private const val TAG = "ChatHelper"
    }
}