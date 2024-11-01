package tech.fourge.huddleup_frontend.Ui

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Helpers.TeamHelper

class FragmentTeamPage : Fragment() {
    private var isBenchVisible = false
    private lateinit var benchScrollView: View
    private lateinit var benchButton: ImageView
    private lateinit var saveButton: Button
    private var originalButtonY: Float = 0f
    private var playerList: List<String> = emptyList()
    private var loading: Boolean = false
    private val teamHelper = TeamHelper()
    private var playerMap = mutableMapOf<Int, String>()  // Stores players by position number
    private var loadingDialog: AlertDialog? = null

    // Mapping position numbers to corresponding ImageView and TextView IDs, and unique drawable resources
    private val positionViewMap = mapOf(
        1 to Triple(R.id.loose_head_prop, R.id.loose_head_prop_text, R.drawable.saved_loose_head_prop),
        2 to Triple(R.id.hooker, R.id.hooker_text, R.drawable.saved_hooker),
        3 to Triple(R.id.tight_head_prop, R.id.tight_head_prop_text, R.drawable.saved_tight_head_prop),
        4 to Triple(R.id.lock_one, R.id.lock_one_text, R.drawable.saved_lock_one),
        5 to Triple(R.id.lock_two, R.id.lock_two_text, R.drawable.saved_lock_two),
        6 to Triple(R.id.blindside_flanker, R.id.blindside_flanker_text, R.drawable.saved_blind_side_flanker),
        7 to Triple(R.id.open_side_flanker, R.id.open_side_flanker_text, R.drawable.saved_open_side_flanker),
        8 to Triple(R.id.number_eight, R.id.number_eight_text, R.drawable.saved_number_eight),
        9 to Triple(R.id.scrum_half, R.id.scrum_half_text, R.drawable.saved_scrum_half),
        10 to Triple(R.id.flyhalf, R.id.flyhalf_text, R.drawable.saved_flyhalf),
        11 to Triple(R.id.left_wing, R.id.left_wing_text, R.drawable.saved_left_wing),
        12 to Triple(R.id.inside_centre, R.id.inside_centre_text, R.drawable.saved_inside_center),
        13 to Triple(R.id.outside_centre, R.id.outside_centre_text, R.drawable.saved_outside_center),
        14 to Triple(R.id.right_wing, R.id.right_wing_text, R.drawable.saved_right_wing),
        15 to Triple(R.id.fullback, R.id.fullback_text, R.drawable.saved_fullback),
        16 to Triple(R.id.sixteen,R.id.sixteen_text , R.drawable.saved_sixteen),
        17 to Triple(R.id.seventeen, R.id.seventeen_text, R.drawable.saved_seventeen),
        18 to Triple(R.id.eighteen, R.id.eighteen_text, R.drawable.saved_eighteen),
        19 to Triple(R.id.nineteen, R.id.nineteen_text, R.drawable.saved_nineteen),
        20 to Triple(R.id.twenty, R.id.twenty_text, R.drawable.saved_twenty),
        21 to Triple(R.id.twenty_one, R.id.twenty_one_text, R.drawable.saved_twenty_one),
        22 to Triple(R.id.twenty_two, R.id.twenty_two_text, R.drawable.saved_twenty_two),
        23 to Triple(R.id.twenty_three, R.id.twenty_three_text, R.drawable.saved_twenty_three)
    )

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
        saveButton = view.findViewById(R.id.topButton)


        // Show loading dialog if loading is true
        if (loading) {
            showLoadingDialog()
        }

        // Fetch the team details and update the player list
        lifecycleScope.launch {
            loading = true
            showLoadingDialog()
            val teamId = CurrentUserUtil.currentUser.teamIds[0]
            playerList = teamHelper.getTeamPlayerNames(teamId)

            // Fetch the team players and save to playerMap
            val teamPlayers = teamHelper.getTeamPlayers(teamId)
            playerMap = teamPlayers.map { (key, value) -> value to key }.toMap().toMutableMap()
            playerList = playerList.filter { playerName -> !playerMap.containsValue(playerName) }
            playerList = playerList.sortedBy { it.split(" ").firstOrNull() }
            loading = false
            dismissLoadingDialog()
            for ((key, value) in playerMap) {
                updatePositionInView(key, value)
            }
        }

        // Save the original Y position of the button
        originalButtonY = benchButton.y

        benchButton.setOnClickListener {
            toggleBenchVisibility()
        }

//        playerMap = CurrentUserUtil.playerMap

        if(CurrentUserUtil.currentUser.role == "Manager"){
            saveButton.visibility = View.VISIBLE
            // Loop through each position number and set up the click listener
            for (positionNumber in positionViewMap.keys) {
                val imageViewId = positionViewMap[positionNumber]?.first
                imageViewId?.let { id ->
                    view.findViewById<ImageView>(id).setOnClickListener {
                        handleClick(positionNumber)
                    }
                }
            }
        }
        else{
            saveButton.visibility = View.GONE
        }

        // Load any previously assigned players into the view
        loadPlayersIntoView()

        // Save button click listener
        saveButton.setOnClickListener {
            lifecycleScope.launch {
                val teamId = CurrentUserUtil.currentUser.teamIds[0]
                val success = teamHelper.updateTeamPlayers(teamId, playerMap.map { (key, value) -> value to key }.toMap())
                if (success) {
                    Toast.makeText(requireContext(), "Players updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to update players", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            val loadingDialogView = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null)
            loadingDialog = AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
                .setView(loadingDialogView)
                .setCancelable(false)
                .create()
        }
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun handleClick(positionNumber: Int) {
        // If there's already a player mapped, reset the position to default
        if (playerMap.containsKey(positionNumber)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Remove Player from Position $positionNumber?")
                .setMessage("Would you like to reset this position to default?")
                .setPositiveButton("Yes") { dialog, _ ->
                    resetPositionToDefault(positionNumber)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            // Show a popup dialog with a list of players if no player is mapped
            val playersArray = playerList.toTypedArray()
            var selectedPlayer = playersArray[0]

            AlertDialog.Builder(requireContext())
                .setTitle("Select Player for Position $positionNumber")
                .setSingleChoiceItems(playersArray, 0) { _, which ->
                    selectedPlayer = playersArray[which]
                }
                .setPositiveButton("Select") { dialog, _ ->
                    // Handle the player selection
                    playerMap[positionNumber] = selectedPlayer
                    updatePositionInView(positionNumber, selectedPlayer)
                    playerList = playerList.filter { it != selectedPlayer }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun updatePositionInView(positionNumber: Int, playerName: String) {
        // Update the ImageView with the position-specific icon
        val imageViewId = positionViewMap[positionNumber]?.first
        val drawableResId = positionViewMap[positionNumber]?.third  // Position-specific drawable
        imageViewId?.let { id ->
            val imageView = view?.findViewById<ImageView>(id)
            drawableResId?.let { imageView?.setImageResource(it) }
        }

        // Update the TextView with the player's name
        val textViewId = positionViewMap[positionNumber]?.second
        textViewId?.let { id ->
            val textView = view?.findViewById<TextView>(id)
            val nameParts = playerName.split(" ")
            val firstName = nameParts.firstOrNull() ?: ""
            val surname = nameParts.drop(1).joinToString(" ")
            textView?.text = "$firstName\n$surname"
        }
    }

    private fun loadPlayersIntoView() {
        for ((positionNumber, playerName) in playerMap) {
            updatePositionInView(positionNumber, playerName)
        }
    }

    private fun resetPositionToDefault(positionNumber: Int) {
        // Check if a player is already mapped to this position
        if (playerMap.containsKey(positionNumber)) {
            // Remove the player from playerMap
            val removedPlayer = playerMap.remove(positionNumber)
            removedPlayer?.let { playerList = playerList + it }
            playerList = playerList.sortedBy { it.split(" ").firstOrNull() }
            // Reset ImageView to default icon
            val imageViewId = positionViewMap[positionNumber]?.first
            val addDrawableResId = getAddDrawableResId(positionNumber)  // Position-specific "add_" drawable
            imageViewId?.let { id ->
                val imageView = view?.findViewById<ImageView>(id)
                addDrawableResId?.let { imageView?.setImageResource(it) }
            }

            // Reset TextView to default position name
            val textViewId = positionViewMap[positionNumber]?.second
            textViewId?.let { id ->
                val textView = view?.findViewById<TextView>(id)
                val defaultName = getDefaultPositionName(positionNumber)
                textView?.text = defaultName
            }
        }
    }

    // Helper function to get default position names
    private fun getDefaultPositionName(positionNumber: Int): String {
        return when (positionNumber) {
            1 -> "Loose Head Prop"
            2 -> "Hooker"
            3 -> "Tight Head Prop"
            4 -> "Lock One"
            5 -> "Lock Two"
            6 -> "Blindside Flanker"
            7 -> "Openside Flanker"
            8 -> "Number Eight"
            9 -> "Scrum Half"
            10 -> "Flyhalf"
            11 -> "Left Wing"
            12 -> "Inside Centre"
            13 -> "Outside Centre"
            14 -> "Right Wing"
            15 -> "Fullback"
            16 -> "Reserve 1"
            17 -> "Reserve 2"
            18 -> "Reserve 3"
            19 -> "Reserve 4"
            20 -> "Reserve 5"
            21 -> "Reserve 6"
            22 -> "Reserve 7"
            23 -> "Reserve 8"
            else -> "Unknown Position"
        }
    }

    private fun getAddDrawableResId(positionNumber: Int): Int? {
        return when (positionNumber) {
            1 -> R.drawable.add_loose_head_prop
            2 -> R.drawable.add_hooker
            3 -> R.drawable.add_tight_head_prop
            4 -> R.drawable.add_lock_one
            5 -> R.drawable.add_lock_two
            6 -> R.drawable.add_blind_side_flanker
            7 -> R.drawable.add_open_side_flanker
            8 -> R.drawable.add_number_eight
            9 -> R.drawable.add_scrum_half
            10 -> R.drawable.add_flyhalf
            11 -> R.drawable.add_left_wing
            12 -> R.drawable.add_inside_center
            13 -> R.drawable.add_outside_center
            14 -> R.drawable.add_right_wing
            15 -> R.drawable.add_fullback
            16 -> R.drawable.add_sixteen
            17 -> R.drawable.add_seventeen
            18 -> R.drawable.add_eighteen
            19 -> R.drawable.add_nineteen
            20 -> R.drawable.add_twenty
            21 -> R.drawable.add_twenty_one
            22 -> R.drawable.add_twenty_two
            23 -> R.drawable.add_twenty_three
            else -> null
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
    ObjectAnimator.ofFloat(benchButton, "translationY", buttonTranslationY.coerceAtMost(originalButtonY)).apply {
        duration = 300
        start()
    }

    // Ensure the button remains visible
    benchButton.visibility = View.VISIBLE

    // Set visibility after animation
    benchScrollView.postDelayed({
        benchScrollView.visibility = if (isBenchVisible) View.VISIBLE else View.GONE
        benchButton.visibility = View.VISIBLE // Ensure the button is visible after the animation
    }, 300)
}
}
