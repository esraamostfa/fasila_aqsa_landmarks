package com.fasila.aqsalandmarks.ui.card

import android.opengl.Visibility
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.databinding.FragmentCardBinding


class CardFragment : Fragment() {

    private lateinit var binding: FragmentCardBinding

    private val args : CardFragmentArgs by navArgs()
    private lateinit var cardId : String

    private lateinit var viewModel: CardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card, container,false)

        cardId = args.stageId
        val application = requireNotNull((this.activity)?.application)
        val viewModelFactory = CardViewModelFactory(application, cardId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CardViewModel::class.java)

        setCardVisibility()
        setCardViews()
        onClickQuizButton()

        return binding.root
    }

    private fun setCardVisibility() {
        binding.cardContainer.setOnClickListener {
            if (binding.cardContent.visibility == GONE) {
                binding.cardContent.visibility = VISIBLE
                binding.cardImage.visibility = GONE
            }else {
                binding.cardContent.visibility = GONE
                binding.cardImage.visibility = VISIBLE
            }
        }
    }

    private fun setCardViews() {
        val imageResource = activity?.resources?.getIdentifier(viewModel.card.value?.uri, null, activity?.packageName)
        binding.cardImage.setImageResource(imageResource!!)

        val cardContent = viewModel.card.value?.content
        binding.cardContent.text = cardContent

        val cardTitle = viewModel.stage.value?.name
        binding.cardTitle.text = cardTitle
    }

    private fun onClickQuizButton() {
        binding.cardQuizButton.setOnClickListener{
            findNavController().navigate(CardFragmentDirections.actionCardFragmentToQuizFragment(cardId))
        }
    }
}