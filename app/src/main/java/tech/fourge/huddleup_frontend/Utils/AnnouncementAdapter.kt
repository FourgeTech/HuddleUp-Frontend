package tech.fourge.huddleup_frontend.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.fourge.huddleup_frontend.Models.AnnouncementModel
import tech.fourge.huddleup_frontend.R //

class AnnouncementAdapter(private val announcements: List<AnnouncementModel>) :
    RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val announcement: AnnouncementModel = announcements[position]
        holder.tvName.text = announcement.name
        holder.tvTime.text = announcement.time
        holder.tvAnnouncementText.text = announcement.announcementText
        holder.tvViews.text = announcement.views
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvAnnouncementText: TextView = itemView.findViewById(R.id.tvAnnouncementText)
        var tvViews: TextView = itemView.findViewById(R.id.tvViews)
        var editReply: EditText = itemView.findViewById(R.id.editReply)
    }
}
