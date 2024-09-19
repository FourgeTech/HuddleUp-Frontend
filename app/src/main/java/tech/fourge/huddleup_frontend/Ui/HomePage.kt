package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import tech.fourge.huddleup_frontend.Models.AnnouncementModel
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.AnnouncementAdapter
import java.net.HttpURLConnection
import java.net.URL

class HomePage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter
    private lateinit var btnAddAnnouncement: Button
    private lateinit var announcementList: List<AnnouncementModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        btnAddAnnouncement = view.findViewById(R.id.btnAddAnnouncement)
        btnAddAnnouncement.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, FragmentCreateAnnouncement())
                addToBackStack(null)
                commit()
            }
        }
        // Inflate the layout for this fragment
        return view
    }


    // Called after onCreateView, when the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)  // Assuming fragment_home_page.xml has a RecyclerView with this id

        // Fetch announcements from API
        fetchAnnouncements()
    }

    private fun fetchAnnouncements() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = getAnnouncements()
                val announcements = parseAnnouncements(response)
                withContext(Dispatchers.Main) {
                    setupRecyclerView(announcements)
                }
            } catch (e: Exception) {
                e.printStackTrace() // handle error
            }
        }
    }

    private fun setupRecyclerView(announcements: List<AnnouncementModel>) {
        adapter = AnnouncementAdapter(announcements)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun getAnnouncements(): String {
        // Replace with your API URL
        val url = URL("https://your-api-url.com/getAnnouncements")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        return connection.inputStream.bufferedReader().use { it.readText() }
    }

    private fun parseAnnouncements(response: String): List<AnnouncementModel> {
        // Parse the JSON response to create a list of AnnouncementModel objects
        val announcementsJsonArray = JSONArray(response)
        val announcementsList = mutableListOf<AnnouncementModel>()

        for (i in 0 until announcementsJsonArray.length()) {
            val jsonObject = announcementsJsonArray.getJSONObject(i)
            val announcement = AnnouncementModel.fromMap(jsonObject.toMap())
            announcementsList.add(announcement)
        }
        return announcementsList
    }

    // Extension function to convert JSONObject to Map<String, Any?>
    private fun JSONObject.toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = keys()

        while (keys.hasNext()) {
            val key = keys.next()
            val value = when (val obj = get(key)) {
                is JSONArray -> obj.toList()
                is JSONObject -> obj.toMap()
                JSONObject.NULL -> null
                else -> obj
            }
            map[key] = value
        }
        return map
    }

    // Extension function to convert JSONArray to List<Any?>
    private fun JSONArray.toList(): List<Any?> {
        val list = mutableListOf<Any?>()
        for (i in 0 until length()) {
            val value = when (val obj = get(i)) {
                is JSONArray -> obj.toList()
                is JSONObject -> obj.toMap()
                JSONObject.NULL -> null
                else -> obj
            }
            list.add(value)
        }
        return list
    }
}