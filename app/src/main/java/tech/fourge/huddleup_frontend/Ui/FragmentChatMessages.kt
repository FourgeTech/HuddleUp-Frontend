package tech.fourge.huddleup_frontend.Ui

import ChatAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.ChatHelper
import tech.fourge.huddleup_frontend.Models.Message
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_CHAT_ID = "chatId"
private const val ARG_CHAT_NAME = "chatName"
private const val ARG_CHAT_MEMBER_COUNT = "chatMemberCount"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentChatMessages.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentChatMessages : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private var messageList: List<Message> = listOf()
    private lateinit var chatId: String
    private lateinit var chatName: String
    private var chatMemberCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatId = it.getString(ARG_CHAT_ID) ?: ""
            chatName = it.getString(ARG_CHAT_NAME) ?: ""
            chatMemberCount = it.getInt(ARG_CHAT_MEMBER_COUNT) ?: 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_messages, container, false)

        lifecycleScope.launch {
            val result  = ChatHelper().loadMessages(chatId)
            messageList = result
            updateRecyclerView()
        }

        view.findViewById<TextView>(R.id.chatName).text = chatName


        view.findViewById<Button>(R.id.buttonRefresh).setOnClickListener {
            lifecycleScope.launch {
                val result  = ChatHelper().loadMessages(chatId)
                messageList = result
                updateRecyclerView()
            }
        }

        view.findViewById<ImageButton>(R.id.buttonSend).setOnClickListener {

            val messagebox = view.findViewById<EditText>(R.id.messageInput)

            lateinit var message: Message
            val messageContent = messagebox.text.toString()
            if (messageContent.isNotEmpty()) {
                message = Message(
                    senderId = CurrentUserUtil.currentUserUID,
                    sender = CurrentUserUtil.currentUser.firstname + " " + CurrentUserUtil.currentUser.lastname,
                    content = messageContent,
                    teamId = "teamId"
                )
                messagebox.text.clear() // Clear the message box after sending
            }

            lifecycleScope.launch {
                ChatHelper().newMessage(chatId, message)
                val result  = ChatHelper().loadMessages(chatId)
                messageList = result
                updateRecyclerView()
            }

        }
        return view
    }

    // Called after onCreateView, when the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewChat)

        // Set up RecyclerView with the adapter
        adapter = ChatAdapter(messageList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    fun updateRecyclerView() {
        // Set up RecyclerView with the adapter
        adapter = ChatAdapter(messageList)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentChat.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(chatId: String, chatName: String, chatMemberCount: Int) =
            FragmentChatMessages().apply {
                arguments = Bundle().apply {
                    putString(ARG_CHAT_ID, chatId)
                    putString(ARG_CHAT_NAME, chatName)
                    putInt(ARG_CHAT_MEMBER_COUNT, chatMemberCount)
                }
            }
    }
}