package com.fasila.aqsalandmarks.ui.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.databinding.FragmentScoreBinding
import java.util.*

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private lateinit var viewModel: ScoreViewModel

    private val args: ScoreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)

//        val lastDay = AqsaLandmarksApplication.sharedPref.getInt("lastDay", 0)
//        val calendar = Calendar.getInstance()
//        val thisDay: Int = calendar.get(Calendar.DAY_OF_YEAR)
        binding.nextButton.setOnClickListener {
            if (args.score >= 2) {

                it.findNavController().navigate(R.id.action_scoreFragment_to_streakFragment)
            } else
                it.findNavController().navigate(R.id.action_scoreFragment_to_stagesFragment)
        }

        setScoreText(args.score)
        setStars(args.score)

        return binding.root
    }

    private fun setStars(score: Int) {
        when (score) {
            0 -> {
                binding.star1.visibility = View.GONE
                binding.star2.visibility = View.GONE
                binding.star3.visibility = View.GONE
            }
            1 -> {
                binding.star1.visibility = View.VISIBLE
                binding.star2.visibility = View.GONE
                binding.star3.visibility = View.GONE
            }
            2 -> {
                binding.star1.visibility = View.VISIBLE
                binding.star2.visibility = View.VISIBLE
                binding.star3.visibility = View.GONE
            }
            else -> {
                binding.star1.visibility = View.VISIBLE
                binding.star2.visibility = View.VISIBLE
                binding.star3.visibility = View.VISIBLE
            }
        }
    }

    private fun setScoreText(score: Int) {
        when(score) {
            0,1 -> binding.scoreText.text = "حاول مرة أخرى"
            2 -> binding.scoreText.text = "لا بأس"
            else -> binding.scoreText.text = "أحسنت"
        }
    }

}