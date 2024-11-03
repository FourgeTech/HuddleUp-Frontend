package tech.fourge.huddleup_frontend.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.fourge.huddleup_frontend.Models.EventModel
import tech.fourge.huddleup_frontend.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventAdapter(private val events: List<EventModel>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event: EventModel = events[position]
        holder.tvEventType.text = event.eventType
        holder.tvDate.text = event.date
        holder.tvTime.text = event.time
        holder.tvLocation.text = event.location
        holder.tvCreatedBy.text = event.createdBy
    }

    override fun getItemCount(): Int {
        return events.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvEventType: TextView = itemView.findViewById(R.id.tvEventType)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        var tvCreatedBy: TextView = itemView.findViewById(R.id.tvCreatedBy)
    }

    private fun formatTimestampToTime(timestamp: String): String {
        val instant = Instant.parse(timestamp)
        val formatter = DateTimeFormatter.ofPattern("d MMMM HH:mm")
            .withZone(ZoneId.of("Africa/Johannesburg"))
        return formatter.format(instant)
    }
}