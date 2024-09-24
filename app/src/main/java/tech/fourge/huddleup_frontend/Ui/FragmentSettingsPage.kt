package tech.fourge.huddleup_frontend.Ui

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.ToggleButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.Models.Settings
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import kotlin.math.log
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil.Companion.currentUserSettings
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil.Companion.currentUserUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSettingsPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSettingsPage : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.context?.let {

            ArrayAdapter.createFromResource(
                it,
                R.array.themes, // Assume you've defined this array in your strings.xml
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                view.findViewById<Spinner>(R.id.editTheme).adapter = adapter
            }

            ArrayAdapter.createFromResource(
                it,
                R.array.languages, // Assume you've defined this array in your strings.xml
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                view.findViewById<Spinner>(R.id.editLanguage).adapter = adapter
            }
        }

        // Load settings from Firestore
        lifecycleScope.launch {
            val settings = UserHelper().getSettings(CurrentUserUtil.currentUserUID)
            Log.d("SettingsLoad", "Settings: $settings")
            if (settings != null) {
                // Update UI with fetched settings
                // Get the array of themes and languages
                val themesArray = resources.getStringArray(R.array.themes)
                val languagesArray = resources.getStringArray(R.array.languages)

                // Get the index of the theme and language from the settings
                val themeIndex = themesArray.indexOf(settings.theme)
                val languageIndex = languagesArray.indexOf(settings.preferredLanguage)

                // Safely set the spinner selections if the index is valid (i.e., not -1)
                if (themeIndex >= 0) {
                    view.findViewById<Spinner>(R.id.editTheme).setSelection(themeIndex)
                }

                if (languageIndex >= 0) {
                    view.findViewById<Spinner>(R.id.editLanguage).setSelection(languageIndex)
                }
                view.findViewById<Switch>(R.id.matchAlertsSwitch).isChecked = settings.matchAlerts
                view.findViewById<Switch>(R.id.practiceAlertsSwitch).isChecked = settings.practiceAlerts
                view.findViewById<Switch>(R.id.chatNotificationsSwitch).isChecked = settings.chatNotifications
            } else {
                Log.d("SettingsLoad", "Failed to fetch settings from Firestore")
            }
        }

        // Set up the click listener for the Save Settings button
        val editSettingsButton: Button = view.findViewById(R.id.buttonSave)
        editSettingsButton.setOnClickListener {
            val matchAlerts = view.findViewById<Switch>(R.id.matchAlertsSwitch).isChecked
            val practiceAlerts = view.findViewById<Switch>(R.id.practiceAlertsSwitch).isChecked
            val chatNotifications = view.findViewById<Switch>(R.id.chatNotificationsSwitch).isChecked
            val editTheme = view.findViewById<Spinner>(R.id.editTheme).selectedItem.toString()
            val editLanguage = view.findViewById<Spinner>(R.id.editLanguage).selectedItem.toString()

            if (editLanguage.isNotEmpty()) {
                CurrentUserUtil.currentUserSettings.preferredLanguage = editLanguage
            }

            if (editTheme.isNotEmpty()){
                CurrentUserUtil.currentUserSettings.theme = editTheme
            }

            CurrentUserUtil.currentUserSettings.matchAlerts = matchAlerts
            CurrentUserUtil.currentUserSettings.practiceAlerts = practiceAlerts
            CurrentUserUtil.currentUserSettings.chatNotifications = chatNotifications


            lifecycleScope.launch {
                // Update the settings in Firestore
                val success = UserHelper().updateSettings(CurrentUserUtil.currentUserUID, CurrentUserUtil.currentUserSettings)
                if (success) {
                    Log.d("SettingsUpdate", "Settings updated successfully")
                } else {
                    Log.d("SettingsUpdate", "Failed to update settings in Firestore")
                }
            }

            // Navigate back to profile page
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentProfilePage())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentProfilePage())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSettingsPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}