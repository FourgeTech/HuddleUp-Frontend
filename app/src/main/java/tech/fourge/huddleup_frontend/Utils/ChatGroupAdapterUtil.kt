package tech.fourge.huddleup_frontend.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.fourge.huddleup_frontend.Models.ChatGroupModel
import tech.fourge.huddleup_frontend.R

class ChatGroupAdapter(private val chats: List<ChatGroupModel>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ChatGroupAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewIcon: ImageView = view.findViewById(R.id.imageViewIcon)
        val textViewChatName: TextView = view.findViewById(R.id.textViewChatName)
        val textViewChatDescription: TextView = view.findViewById(R.id.textViewChatDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_group, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.textViewChatName.text = chat.chatName
        holder.textViewChatDescription.text = chat.description

        // Set the click listener for the entire item view
        holder.itemView.setOnClickListener {
            listener.onItemClick(chat) // Pass the clicked chat model to the listener
        }
    }

    // Define an interface for the click listener
    interface OnItemClickListener {
        fun onItemClick(chat: ChatGroupModel)
    }

    override fun getItemCount() = chats.size
}