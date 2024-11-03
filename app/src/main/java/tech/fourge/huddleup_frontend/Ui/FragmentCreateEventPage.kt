package tech.fourge.huddleup_frontend.Ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.EventHelper
import tech.fourge.huddleup_frontend.Models.EventModel
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil.Companion.currentUser
import java.text.SimpleDateFormat
import java.util.*

class FragmentCreateEventPage : Fragment() {

    private lateinit var spinnerEventType: Spinner
    private lateinit var editTextDate: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var buttonCreateEvent: Button
    private val eventHelper = EventHelper() // Initialize your helper class

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.create_event, container, false)

        // Initialize your UI elements
        spinnerEventType = view.findViewById(R.id.spinnerEventType)
        editTextDate = view.findViewById(R.id.editTextDate)
        editTextTime = view.findViewById(R.id.editTextTime)
        editTextLocation = view.findViewById(R.id.editTextLocation)
        buttonCreateEvent = view.findViewById(R.id.buttonCreateEvent)

        // Set up the spinner with event types
        val eventTypes = arrayOf("Match", "Practice")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventType.adapter = adapter

        // Set up date picker
        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up time picker
        editTextTime.setOnClickListener {
            showTimePickerDialog()
        }

        // Set up the button click listener
        buttonCreateEvent.setOnClickListener {
            createEvent()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                editTextDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                editTextTime.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun createEvent() {
        val eventType = spinnerEventType.selectedItem.toString()
        val date = editTextDate.text.toString().trim()
        val time = editTextTime.text.toString().trim()
        val location = editTextLocation.text.toString().trim()

        if (date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a new eventId and current timestamp
        val eventId = UUID.randomUUID().toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Africa/Johannesburg")
        val currentTime = dateFormat.format(Date())

        // Create the EventModel object
        val event = EventModel(
            eventId = eventId,
            teamId = currentUser.teamIds[0],
            eventType = eventType,
            date = date,
            time = time,
            location = location,
            createdBy = currentUser.username,
            )

        // Call the createEvent method in a coroutine
        lifecycleScope.launch {
            val success = eventHelper.createEvent(event)
            if (success) {
                Toast.makeText(context, "Event created successfully.", Toast.LENGTH_SHORT).show()
                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragment_container, FragmentEventPage())
                    addToBackStack(null)
                    commit()
                }
            } else {
                Toast.makeText(context, "Failed to create event. Please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }
}