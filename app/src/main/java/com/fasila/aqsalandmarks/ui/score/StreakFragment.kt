package com.fasila.aqsalandmarks.ui.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.databinding.FragmentStreakBinding

class StreakFragment : Fragment() {

    lateinit var binding: FragmentStreakBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_streak, container, false)

        val streak = AqsaLandmarksApplication.sharedPref.getInt("streakCounter", 0)
        binding.streak.text = streak.toString()

        binding.streakContainer.setOnClickListener {
            it.findNavController().navigate(R.id.action_streakFragment_to_stagesFragment)
        }

        return binding.root
    }
}