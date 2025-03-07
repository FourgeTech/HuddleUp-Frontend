package tech.fourge.huddleup_frontend.Utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.fourge.huddleup_frontend.Models.AnnouncementModel
import tech.fourge.huddleup_frontend.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AnnouncementAdapter(private val announcements: List<AnnouncementModel>) :
    RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("HomePage", "Fetched Announcements: $announcements")

        val announcement: AnnouncementModel = announcements[position]
        holder.tvName.text = announcement.createdBy
        holder.tvTime.text = formatTimestampToTime(announcement.createdAt)
        holder.tvAnnouncementText.text = announcement.message
        holder.tvViews.text = "${announcement.viewCount}" + "/10"
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvAnnouncementText: TextView = itemView.findViewById(R.id.tvAnnouncementText)
        var tvViews: TextView = itemView.findViewById(R.id.tvViews)
    }

    fun formatTimestampToTime(timestamp: String): String {

        // Parse the timestamp string to Instant
        val instant = Instant.parse(timestamp)

        // Correct the DateTimeFormatter pattern
        val formatter = DateTimeFormatter.ofPattern("d MMMM HH:mm")
            .withZone(ZoneId.of("Africa/Johannesburg"))

        return formatter.format(instant)
    }

}
