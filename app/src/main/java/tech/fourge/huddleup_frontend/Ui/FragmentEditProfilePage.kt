package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentEditProfilePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentEditProfilePage : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_edit_profile_page, container, false)

        // Set up the click listener for the Edit Profile button
        val editProfileButton: Button = view.findViewById(R.id.save_button)
        editProfileButton.setOnClickListener {
            val firstname = view.findViewById<EditText>(R.id.edit_first_name).text.toString()
            val lastname = view.findViewById<EditText>(R.id.edit_last_name).text.toString()
            val phoneNumber = view.findViewById<EditText>(R.id.edit_phone_number).text.toString()
            val username = view.findViewById<EditText>(R.id.edit_username).text.toString()

            CurrentUserUtil.currentUser.firstname = firstname
            CurrentUserUtil.currentUser.lastname = lastname
            CurrentUserUtil.currentUser.phoneNumber = phoneNumber
            CurrentUserUtil.currentUser.username = username

            lifecycleScope.launch {
                val currentUserUID = CurrentUserUtil.currentUserUID
                val currentUser = CurrentUserUtil.currentUser

                try {
                    UserHelper().updateUser(currentUserUID, currentUser)
                    UserHelper().getUser(CurrentUserUtil.currentUserUID!!)
                } catch (e: Exception) {
                    Log.d(TAG, "Error during user update: ${e.message}")
                }
            }
        }
        return view
    }

    companion object {
        private const val TAG = "FragmentEditProfilePage"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentEditProfilePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentEditProfilePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}