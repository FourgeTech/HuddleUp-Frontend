package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.Utils.openIntent
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.fourge.huddleup_frontend.Helpers.ChatHelper
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.databinding.HomePageBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : HomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomePage())

//        lifecycleScope.launch {
//            ChatHelper().newChat("Team Chat", "Team Chat", "Manager")
//        }
        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.locker_room -> replaceFragment(HomePage())
                R.id.team -> replaceFragment(FragmentTeamPage())
                R.id.attendance -> replaceFragment(FragmentEventPage())
                R.id.chat -> replaceFragment(FragmentChatPage())
                R.id.profile -> replaceFragment(FragmentProfilePage())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }
}