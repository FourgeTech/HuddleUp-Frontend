package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.fourge.huddleup_frontend.Helpers.AnnouncementHelper
import tech.fourge.huddleup_frontend.Models.AnnouncementModel
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.AnnouncementAdapter
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil.Companion.currentUser

class HomePage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter
    private lateinit var btnAddAnnouncement: Button
    private lateinit var announcementHelper: AnnouncementHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        btnAddAnnouncement = view.findViewById(R.id.btnAddAnnouncement)
        if(currentUser.role != "Manager"){
            btnAddAnnouncement.visibility = View.GONE
        }
        else{
            btnAddAnnouncement.visibility = View.VISIBLE
        }
        btnAddAnnouncement.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, FragmentCreateAnnouncement())
                addToBackStack(null)
                commit()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize AnnouncementHelper
        announcementHelper = AnnouncementHelper()

        // Fetch announcements and set up RecyclerView
        lifecycleScope.launch {
            fetchAndDisplayAnnouncements()
        }
    }

    private suspend fun fetchAndDisplayAnnouncements() {
        // Call the suspending function to fetch announcements
        val teamId = CurrentUserUtil.currentUser.teamIds[0]
        val announcements = announcementHelper.getAnnouncementsByTeamId(teamId)

        // Update the UI on the main thread
        withContext(Dispatchers.Main) {
            announcements?.let {
                setupRecyclerView(it)
            }
        }
    }

    private fun setupRecyclerView(announcements: List<AnnouncementModel>) {
        if (announcements.isNotEmpty()) {
            adapter = AnnouncementAdapter(announcements)
            recyclerView.adapter = adapter
        } else {
            Log.d("HomePage", "No announcements found.")
        }
    }
}
