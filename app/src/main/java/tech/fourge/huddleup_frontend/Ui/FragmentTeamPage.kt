package tech.fourge.huddleup_frontend.Ui

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Helpers.TeamHelper
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FragmentTeamPage : Fragment() {
    private var isBenchVisible = false
    private lateinit var benchScrollView: View
    private lateinit var benchButton: ImageView
    private var originalButtonY: Float = 0f
    private var playerList: List<String> = emptyList()
    private var loading: Boolean = false
    private val teamHelper = TeamHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        benchButton = view.findViewById(R.id.benchButton)
        benchScrollView = view.findViewById(R.id.benchScrollView)

        // Fetch the team details and update the player list
        lifecycleScope.launch {
            loading = true
            val teamId = CurrentUserUtil.currentUser.teamIds[0]
            playerList = teamHelper.getTeamPlayerNames(teamId)
            loading = false
        }

        // Save the original Y position of the button
        originalButtonY = benchButton.y

        benchButton.setOnClickListener {
            toggleBenchVisibility()
        }

        // Array of IDs for the player ImageViews and bench slots
        val imageViewIds = arrayOf(
            R.id.loose_head_prop,
            R.id.hooker,
            R.id.tight_head_prop,
            R.id.lock_one,
            R.id.lock_two,
            R.id.blindside_flanker,
            R.id.number_eight,
            R.id.open_side_flanker,
            R.id.scrum_half,
            R.id.flyhalf,
            R.id.inside_centre,
            R.id.outside_centre,
            R.id.left_wing,
            R.id.right_wing,
            R.id.fullback,
            R.id.sixteen,
            R.id.seventeen,
            R.id.eighteen,
            R.id.nineteen,
            R.id.twenty,
            R.id.twenty_one,
            R.id.twenty_two,
            R.id.twenty_three
        )

        // Loop through each ID and set up the click listener
        for (id in imageViewIds) {
            view.findViewById<ImageView>(id).setOnClickListener {
                handleClick(it.id)
            }
        }
    }

    private fun handleClick(imageViewId: Int) {
        if (!loading) {
            val name = when (imageViewId) {
                R.id.loose_head_prop -> "Loose Head Prop"
                R.id.hooker -> "Hooker"
                R.id.tight_head_prop -> "Tight Head Prop"
                R.id.lock_one -> "Lock One"
                R.id.lock_two -> "Lock Two"
                R.id.blindside_flanker -> "Blindside Flanker"
                R.id.number_eight -> "Number Eight"
                R.id.open_side_flanker -> "Open Side Flanker"
                R.id.scrum_half -> "Scrum Half"
                R.id.flyhalf -> "Flyhalf"
                R.id.inside_centre -> "Inside Centre"
                R.id.outside_centre -> "Outside Centre"
                R.id.left_wing -> "Left Wing"
                R.id.right_wing -> "Right Wing"
                R.id.fullback -> "Fullback"
                R.id.sixteen -> "Bench Slot 1"
                R.id.seventeen -> "Bench Slot 2"
                R.id.eighteen -> "Bench Slot 3"
                R.id.nineteen -> "Bench Slot 4"
                R.id.twenty -> "Bench Slot 5"
                R.id.twenty_one -> "Bench Slot 6"
                R.id.twenty_two -> "Bench Slot 7"
                R.id.twenty_three -> "Bench Slot 8"
                else -> "Unknown Position"
            }

            // Show a popup dialog with a list of players
            val playersArray = playerList.toTypedArray()
            var selectedPlayer = playersArray[0]


            AlertDialog.Builder(requireContext())
                .setTitle("Select Player for $name")
                .setSingleChoiceItems(playersArray, 0) { _, which ->
                    selectedPlayer = playersArray[which]
                }
                .setPositiveButton("Select") { dialog, _ ->
                    // Handle the player selection
                    updateImageView(imageViewId)
                    updateTextView(imageViewId, selectedPlayer)
                    playerList = playerList.filter { it != selectedPlayer }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            return

        }
    }

    private fun updateImageView(imageViewId: Int) {
        val savedIconResId = when (imageViewId) {
            R.id.loose_head_prop -> R.drawable.saved_loose_head_prop
            R.id.hooker -> R.drawable.saved_hooker
            R.id.tight_head_prop -> R.drawable.saved_tight_head_prop
            R.id.lock_one -> R.drawable.saved_lock_one
            R.id.lock_two -> R.drawable.saved_lock_two
            R.id.blindside_flanker -> R.drawable.saved_blind_side_flanker
            R.id.number_eight -> R.drawable.saved_number_eight
            R.id.open_side_flanker -> R.drawable.saved_open_side_flanker
            R.id.scrum_half -> R.drawable.saved_scrum_half
            R.id.flyhalf -> R.drawable.saved_flyhalf
            R.id.inside_centre -> R.drawable.saved_inside_center
            R.id.outside_centre -> R.drawable.saved_outside_center
            R.id.left_wing -> R.drawable.saved_left_wing
            R.id.right_wing -> R.drawable.saved_right_wing
            R.id.fullback -> R.drawable.saved_fullback
            R.id.sixteen -> R.drawable.saved_sixteen
            R.id.seventeen -> R.drawable.saved_seventeen
            R.id.eighteen -> R.drawable.saved_eighteen
            R.id.nineteen -> R.drawable.saved_nineteen
            R.id.twenty -> R.drawable.saved_twenty
            R.id.twenty_one -> R.drawable.saved_twenty_one
            R.id.twenty_two -> R.drawable.saved_twenty_two
            R.id.twenty_three -> R.drawable.saved_twenty_three
            else -> R.drawable.ic_player
        }

        view?.findViewById<ImageView>(imageViewId)?.setImageResource(savedIconResId)
    }

    private fun updateTextView(imageViewId: Int, playerName: String) {
        val textViewId = when (imageViewId) {
            R.id.loose_head_prop -> R.id.loose_head_prop_text
            R.id.hooker -> R.id.hooker_text
            R.id.tight_head_prop -> R.id.tight_head_prop_text
            R.id.lock_one -> R.id.lock_one_text
            R.id.lock_two -> R.id.lock_two_text
            R.id.blindside_flanker -> R.id.blindside_flanker_text
            R.id.number_eight -> R.id.number_eight_text
            R.id.open_side_flanker -> R.id.open_side_flanker_text
            R.id.scrum_half -> R.id.scrum_half_text
            R.id.flyhalf -> R.id.flyhalf_text
            R.id.inside_centre -> R.id.inside_centre_text
            R.id.outside_centre -> R.id.outside_centre_text
            R.id.left_wing -> R.id.left_wing_text
            R.id.right_wing -> R.id.right_wing_text
            R.id.fullback -> R.id.fullback_text
            else -> null
        }

        textViewId?.let {
            val textView = view?.findViewById<TextView>(it)
            textView?.text = playerName
        }
    }

    private fun toggleBenchVisibility() {
        isBenchVisible = !isBenchVisible
        val translationY = if (isBenchVisible) 0f else benchScrollView.height.toFloat()

        // Animate the scroll view
        ObjectAnimator.ofFloat(benchScrollView, "translationY", translationY).apply {
            duration = 300
            start()
        }

        // Animate the button
        val buttonTranslationY = if (isBenchVisible) originalButtonY else originalButtonY + benchScrollView.height
        ObjectAnimator.ofFloat(benchButton, "translationY", buttonTranslationY).apply {
            duration = 300
            start()
        }

        // Ensure the button remains visible
        benchButton.visibility = View.VISIBLE

        // Set visibility after animation
        benchScrollView.postDelayed({
            benchScrollView.visibility = if (isBenchVisible) View.VISIBLE else View.GONE
        }, 300)
    }

}