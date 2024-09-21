package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.AnnouncementHelper
import tech.fourge.huddleup_frontend.Models.AnnouncementModel
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import java.text.SimpleDateFormat
import java.util.*

class FragmentCreateAnnouncement : Fragment() {

    private lateinit var etMessage: EditText
    private lateinit var btnSubmit: Button
    private val announcementHelper = AnnouncementHelper() // Initialize your helper class
    private val currentUser = CurrentUserUtil.currentUser // Get the current user from CurrentUserUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_announcement, container, false)

        // Initialize your UI elements
        etMessage = view.findViewById(R.id.etMessage)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        // Set up the button click listener
        btnSubmit.setOnClickListener {
            createAnnouncement()
        }

        return view
    }

    private fun createAnnouncement() {
        val message = etMessage.text.toString().trim()

        if (message.isEmpty()) {
            etMessage.error = "Please enter a message"
            return
        }

        // Generate a new announcementId and current timestamp
        val announcementId = UUID.randomUUID().toString()
        val currentTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())

        // Create the AnnouncementModel object
        val announcement = AnnouncementModel(
            announcementId = announcementId,
            teamId = "Tigers",
            title = "${currentUser.firstname} ${currentUser.lastname}",
            message = message,
            createdBy = currentUser.username,
            createdAt = currentTime,
            viewCount = 0
        )

        // Call the createAnnouncement method in a coroutine
        lifecycleScope.launch {
            val success = announcementHelper.createAnnouncement(announcement)
            if (success) {
                Log.d("FragmentCreateAnnouncement", "Announcement created successfully.")
                Toast.makeText(context, "Announcement created successfully.", Toast.LENGTH_SHORT).show()
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragment_container, HomePage())
                    addToBackStack(null)
                    commit()
                }
            } else {
                Log.e("FragmentCreateAnnouncement", "Failed to create announcement.")
                Toast.makeText(context, "Failed to create announcement. Please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
