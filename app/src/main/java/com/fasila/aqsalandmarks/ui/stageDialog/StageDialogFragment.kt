package com.fasila.aqsalandmarks.ui.stageDialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fasila.aqsalandmarks.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class StageDialogFragment : DialogFragment() {

    private lateinit var viewModel: StageDialogViewModel

    private val args : StageDialogFragmentArgs by navArgs()
    private lateinit var stageId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stageId =  args.stageId

        val application = requireNotNull((this.activity)?.application)
        val viewModelFactory = StageDialogViewModelFactory(application, stageId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StageDialogViewModel::class.java)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val stageName = viewModel.stage.value?.name

        val footers = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95, 100)

        if (stageId.toInt() in footers) {
            return MaterialAlertDialogBuilder(requireContext()
                , R.style.ThemeOverlay_App_StageDialog)
                .setTitle(stageName)
                .setNeutralButton("  ابدأ الاختبار  ") { dialog, _ ->
                    this.findNavController().navigate(StageDialogFragmentDirections.actionStageDialogFragmentToQuizFragment(stageId))
                    dialog.dismiss()
                }
                .create()
        }

        return MaterialAlertDialogBuilder(requireContext()
            , R.style.ThemeOverlay_App_StageDialog)
            .setTitle(stageName)
            .setPositiveButton("  انتقل للاختبار   ") { dialog, _ ->
                this.findNavController().navigate(StageDialogFragmentDirections.actionStageDialogFragmentToQuizFragment(stageId))
                dialog.dismiss()
            }
            .setNeutralButton(" اطلع على البطاقة ") { dialog, _ ->
                this.findNavController().navigate(StageDialogFragmentDirections.actionStageDialogFragmentToCardFragment(stageId))
                dialog.dismiss()
            }
            .create()
    }
}