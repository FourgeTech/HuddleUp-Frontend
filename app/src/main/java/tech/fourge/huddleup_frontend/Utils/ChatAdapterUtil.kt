import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import tech.fourge.huddleup_frontend.Models.Message
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil.Companion.currentUserUID
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer)
        val sender: TextView = itemView.findViewById(R.id.sender)
        val messageContent: TextView = itemView.findViewById(R.id.messageContent)
        val timestamp: TextView = itemView.findViewById(R.id.messageTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        // Align the message based on whether it's sent or received
        val layoutParams = holder.messageContainer.layoutParams as LinearLayout.LayoutParams
        if (message.senderId == currentUserUID) {
            // Sent message - align to right
            layoutParams.gravity = Gravity.END
            holder.messageContainer.setBackgroundResource(R.drawable.sent_message_background)
        } else {
            // Received message - align to left
            layoutParams.gravity = Gravity.START
            holder.messageContainer.setBackgroundResource(R.drawable.received_message_background)
        }
        holder.sender.text = message.sender
        holder.messageContent.text = message.content
        if (message.timestamp != null) {
            holder.timestamp.text = formatTimestampToTime(message.timestamp!!)
        }
    }

    fun formatTimestampToTime(timestamp: String): String {

        // Parse the timestamp string to Instant
        val instant = Instant.parse(timestamp)

        // Format the instant to a time string (e.g., "HH:mm:ss")
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
            .withZone(ZoneId.systemDefault()) // Use the system's default timezone

        return formatter.format(instant)
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
