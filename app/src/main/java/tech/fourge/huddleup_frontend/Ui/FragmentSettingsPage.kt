package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.ToggleButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
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
    // TODO: Rename and change types of parameters
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
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings_page, container, false) // Must change!!

        view.findViewById<Button>(R.id.buttonSave).setOnClickListener {

            val matchAlerts = view.findViewById<Switch>(R.id.matchAlertsSwitch).isChecked
            val practiceAlerts = view.findViewById<Switch>(R.id.practiceAlertsSwitch).isChecked
            val chatNotifications = view.findViewById<Switch>(R.id.chatNotificationsSwitch).isChecked
            val editTheme = view.findViewById<EditText>(R.id.editTheme).text.toString()
            val editLanguage = view.findViewById<EditText>(R.id.editLanguage).text.toString()

            CurrentUserUtil.currentUserSettings.matchAlerts = matchAlerts
            CurrentUserUtil.currentUserSettings.practiceAlerts = practiceAlerts
            CurrentUserUtil.currentUserSettings.chatNotifications = chatNotifications
            CurrentUserUtil.currentUserSettings.preferredLanguage = editLanguage
            CurrentUserUtil.currentUserSettings.theme = editTheme

            lifecycleScope.launch{
                UserHelper().updateSettings(currentUserUID, currentUserSettings)
                UserHelper().getSettings(currentUserUID)
            }
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
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSettingsPage.
         */
        // TODO: Rename and change types and number of parameters
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