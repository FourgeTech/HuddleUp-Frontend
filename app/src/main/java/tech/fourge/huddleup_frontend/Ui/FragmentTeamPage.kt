package tech.fourge.huddleup_frontend.Ui

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
            val teamId = CurrentUserUtil.currentUser.teamIds[0]
            Log.d("BITCH", "Team ID: $teamId")
            playerList = teamHelper.getTeamPlayerNames(teamId)
        }

        // Save the original Y position of the button
        originalButtonY = benchButton.y

        benchButton.setOnClickListener {
            toggleBenchVisibility()
        }

        // Array of IDs for the player ImageViews (excluding the team view)
        val playerImageViewIds = arrayOf(
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
            R.id.fullback
        )

        // Loop through each ID and set up the click listener
        for (id in playerImageViewIds) {
            view.findViewById<ImageView>(id).setOnClickListener {
                handlePlayerClick(it.id)
            }
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

    private fun handlePlayerClick(imageViewId: Int) {
        val playerName = when (imageViewId) {
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
            else -> "Unknown Position"
        }

        // Show a popup dialog with a list of players
        val playersArray = playerList.toTypedArray()
        var selectedPlayer = playersArray[0]

        AlertDialog.Builder(requireContext())
            .setTitle("Select Player for $playerName")
            .setSingleChoiceItems(playersArray, 0) { _, which ->
                selectedPlayer = playersArray[which]
            }
            .setPositiveButton("Select") { dialog, _ ->
                // Handle the player selection
                // For example, you can update the UI or save the selection
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}