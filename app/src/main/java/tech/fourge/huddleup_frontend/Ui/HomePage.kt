package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.AnnouncementAdapter
import tech.fourge.huddleup_frontend.Utils.AnnouncementModel

class HomePage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter
    private lateinit var announcementList: List<AnnouncementModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the list of announcements
        announcementList = listOf(
            AnnouncementModel("Aidan Smith", "7min", "Practice Rescheduled for 5PM", "3 / 10"),
            AnnouncementModel("Aidan Smith", "18min", "Scrum practice, don't forget scrum caps", "8 / 10")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    // Called after onCreateView, when the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)  // Assuming fragment_home_page.xml has a RecyclerView with this id

        // Set up RecyclerView with the adapter
        adapter = AnnouncementAdapter(announcementList)
        recyclerView.layoutManager = LinearLayoutManager(context)  // Use context instead of 'this' in fragments
        recyclerView.adapter = adapter
    }
}
