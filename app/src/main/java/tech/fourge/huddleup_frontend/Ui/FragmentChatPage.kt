package tech.fourge.huddleup_frontend.Ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.ChatHelper
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Models.ChatGroupModel
import tech.fourge.huddleup_frontend.Models.Message
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.ChatGroupAdapter
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentChatPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentChatPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatGroupAdapter
    private var chatsList: List<ChatGroupModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_chat_page, container, false)

        lifecycleScope.launch {
            val result  = ChatHelper().getUserChats(CurrentUserUtil.currentUserUID)
            chatsList = result
            updateRecyclerView()
        }

        view.findViewById<View>(R.id.buttonNewChat).setOnClickListener {
            showPopUp(view)
        }
        return view
    }

    fun showPopUp (view: View) {
        val layoutInflater = LayoutInflater.from(this.context)
        val view = layoutInflater.inflate(R.layout.layout_enter_team, null)

        val teamNameEditText: EditText = view.findViewById(R.id.editTextTeamName)
        val chatTypeSpinner: Spinner = view.findViewById(R.id.spinnerChatType)
        val chatDescriptionEditText: EditText = view.findViewById(R.id.editTextDescription)

        // Setting up the spinner
        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.chat_types, // Assume you've defined this array in your strings.xml
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                chatTypeSpinner.adapter = adapter
            }
        }

        AlertDialog.Builder(this.context)
            .setView(view)
            .setTitle("Enter Team Info")
            .setPositiveButton("Save") { dialog, id ->
                val teamName = teamNameEditText.text.toString()
                val chatType = chatTypeSpinner.selectedItem.toString()
                val chatDescription = chatDescriptionEditText.text.toString()
                lifecycleScope.launch {
                   val newChat = ChatHelper().newChat(teamName,chatDescription, CurrentUserUtil.currentUser.teamIds[0], chatType)
                    ChatHelper().addChatIdToUsers(null, newChat)
                    val newChatList  = ChatHelper().getUserChats(CurrentUserUtil.currentUserUID)
                    chatsList = newChatList
                    updateRecyclerView()
                }
            }
            .setNegativeButton("Cancel", { dialog, id -> dialog.cancel() })
            .create()
            .show()
    }

    // Called after onCreateView, when the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewChatGroups)

        // Set up RecyclerView with the adapter
        adapter = ChatGroupAdapter(chatsList,object : ChatGroupAdapter.OnItemClickListener {
            override fun onItemClick(chat: ChatGroupModel) {
                // Handle the click event for the chat item
                // For example, start a new activity or show a dialog
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    fun updateRecyclerView() {
        // Set up RecyclerView with the adapter
        adapter = ChatGroupAdapter(chatsList,object : ChatGroupAdapter.OnItemClickListener {
            override fun onItemClick(chat: ChatGroupModel) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FragmentChatMessages.newInstance(chat.chatId, chat.chatName, chat.members.size))
                    .addToBackStack(null)
                    .commit()
            }
        })
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
         * @return A new instance of fragment FragmentChatPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentChatPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}