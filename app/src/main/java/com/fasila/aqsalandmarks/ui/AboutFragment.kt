package com.fasila.aqsalandmarks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)


        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_aboutFragment_to_stagesFragment)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}