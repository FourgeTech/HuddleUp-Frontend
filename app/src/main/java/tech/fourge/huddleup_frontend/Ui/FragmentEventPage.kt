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
import tech.fourge.huddleup_frontend.Helpers.EventHelper
import tech.fourge.huddleup_frontend.Models.EventModel
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
//import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil.Companion.currentUser
import tech.fourge.huddleup_frontend.Utils.EventAdapter

class FragmentEventPage : Fragment() {

    private lateinit var createEventButton: Button
    private lateinit var matchRecyclerView: RecyclerView
    private lateinit var practiceRecyclerView: RecyclerView
    private lateinit var matchAdapter: EventAdapter
    private lateinit var practiceAdapter: EventAdapter
    private lateinit var eventHelper: EventHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val currentUser = CurrentUserUtil.currentUser
        val view = inflater.inflate(R.layout.fragment_event_page, container, false)
        createEventButton = view.findViewById(R.id.createEventButton)
        if (currentUser.role != "Manager") {
            createEventButton.visibility = View.GONE
        } else {
            createEventButton.visibility = View.VISIBLE
        }
        createEventButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, FragmentCreateEventPage())
                addToBackStack(null)
                commit()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        matchRecyclerView = view.findViewById(R.id.matchRecyclerView)
        practiceRecyclerView = view.findViewById(R.id.practiceRecyclerView)
        matchRecyclerView.layoutManager = LinearLayoutManager(context)
        practiceRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize EventHelper
        eventHelper = EventHelper()

        // Fetch events and set up RecyclerView
        lifecycleScope.launch {
            fetchAndDisplayEvents()
        }
    }

    private suspend fun fetchAndDisplayEvents() {
        // Call the suspending function to fetch events
        val teamId = CurrentUserUtil.currentUser.teamIds[0]
        val events = eventHelper.getEventsByTeamId(teamId)
        Log.d("FragmentEventPage", "Events: $events")

        // Update the UI on the main thread
        withContext(Dispatchers.Main) {
            events?.let {
                setupRecyclerView(it)
            }
        }
    }

    private fun setupRecyclerView(events: List<EventModel>) {
        val matchEvents = events.filter { it.eventType == "Match" }
        val practiceEvents = events.filter { it.eventType == "Practice" }

        if (matchEvents.isNotEmpty()) {
            matchAdapter = EventAdapter(matchEvents)
            matchRecyclerView.adapter = matchAdapter
        } else {
            Log.d("FragmentEventPage", "No match events found.")
        }

        if (practiceEvents.isNotEmpty()) {
            practiceAdapter = EventAdapter(practiceEvents)
            practiceRecyclerView.adapter = practiceAdapter
        } else {
            Log.d("FragmentEventPage", "No practice events found.")
        }
    }
}