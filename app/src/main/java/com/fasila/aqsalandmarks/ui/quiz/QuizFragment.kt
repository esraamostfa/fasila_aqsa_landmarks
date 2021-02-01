package com.fasila.aqsalandmarks.ui.quiz

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.databinding.FragmentQuizBinding
import com.fasila.aqsalandmarks.model.quiz.Quiz


class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding

    private lateinit var viewModel: QuizViewModel
    private val args: QuizFragmentArgs by navArgs()
    private lateinit var cardId: String

    private lateinit var quizzes: MutableList<Quiz>
    lateinit var answers: MutableList<String>
    lateinit var currentQuiz: Quiz
    private var questionIndex = 0
    var correctAnswers = 0
    lateinit var sharedPref: SharedPreferences
    private var hearts = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)

        sharedPref = AqsaLandmarksApplication.sharedPref
        hearts = sharedPref.getString("hearts", "5")?.toInt()!!
        //binding.heartsNum.text = args
        binding.heartsNum.text = hearts.toString()

        cardId = args.cardId
        val application = requireNotNull((this.activity)?.application)
        val viewModelFactory = QuizViewModelFactory(application, cardId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(QuizViewModel::class.java)

        setQuestionImage()
        setQuizCancelButton()

        binding.quiz = this
        quizzes = viewModel.quizzes.value?.toMutableList()!!
        val numQuizzes = quizzes.size.coerceAtMost(3)
        setQuizzes()

        binding.answersRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == -1) {
                binding.submitButton.isEnabled = false
            } else {
                binding.submitButton.isEnabled = true
                binding.submitButton.setOnClickListener {
                    disableChoose()
                    updateQuizProgress()
                    var answerIndex = 0
                    when (checkedId) {
                        R.id.answer2_radioButton2 -> answerIndex = 1
                        R.id.answer3_radioButton3 -> answerIndex = 2
                    }
                    //Correct Answer
                    if (answers[answerIndex] == currentQuiz.correctAnswer) {
                        heartsNum(true)
                        correctAnswers += 1
                        questionIndex++
                        setCheckedButtonColor(Color.GREEN)
                        Toast.makeText(activity, "إجابة صحيحة:)", Toast.LENGTH_SHORT).show()
                        binding.submitButton.visibility = GONE
                        binding.nextButton.visibility = VISIBLE

                        binding.nextButton.setOnClickListener {
                            unCheckButtons()
                            enableChoose()
                            if (questionIndex < numQuizzes) {
                                nextQuestion()
                                binding.submitButton.visibility = VISIBLE
                                binding.nextButton.visibility = GONE

                            } else {
                                openNextStage(correctAnswers)
                                updateStageScore(setScore(correctAnswers))
                                it.findNavController().navigate(
                                    QuizFragmentDirections
                                        .actionQuizFragmentToScoreFragment(setScore(correctAnswers))
                                )
                            }
                        }

                        //Wrong Answer
                    } else {
                        questionIndex++
                        setCheckedButtonColor(Color.RED)
                        Toast.makeText(activity, "إجابة خاطئة):", Toast.LENGTH_SHORT).show()
                        heartsNum(false)
                        binding.submitButton.visibility = GONE
                        binding.nextButton.visibility = VISIBLE

                        binding.nextButton.setOnClickListener {
                            unCheckButtons()
                            enableChoose()
                            if (hearts == 0) {
                                Toast.makeText(activity, "للأسف نفذت فرصك!", Toast.LENGTH_SHORT)
                                    .show()
                                updateStageScore(setScore(correctAnswers))
                                it.findNavController().navigate(
                                    QuizFragmentDirections
                                        .actionQuizFragmentToScoreFragment(setScore(correctAnswers))
                                )
                            } else if (questionIndex < numQuizzes && hearts > 0) {
                                nextQuestion()
                                binding.submitButton.visibility = VISIBLE
                                binding.nextButton.visibility = GONE
                            } else {
                                updateStageScore(setScore(correctAnswers))
                                it.findNavController().navigate(
                                    QuizFragmentDirections
                                        .actionQuizFragmentToScoreFragment(setScore(correctAnswers))
                                )
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun setQuestionImage() {
        val card = viewModel.card.value
        val imageResource =
            activity?.resources?.getIdentifier(card?.uri, null, activity?.packageName)
        binding.quizImage.setImageResource(imageResource!!)
    }

    private fun setQuizCancelButton() {
        binding.quizCancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_stagesFragment)
        }
    }

    private fun setQuestion() {
        currentQuiz = quizzes[questionIndex]
        answers = currentQuiz.answers.toMutableList()
        //answers.shuffle()
        setCheckedButtonColor(R.color.TextColorPrimary)
    }

    private fun setQuizzes() {
        quizzes.shuffle()
        setQuestion()
        questionIndex = 0
    }

    private fun setCheckedButtonColor(checked: Int, unChecked: Int = R.color.TextColorPrimary) {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(unChecked, checked)
        )
        binding.answer1RadioButton.buttonTintList = colorStateList
        binding.answer2RadioButton2.buttonTintList = colorStateList
        binding.answer3RadioButton3.buttonTintList = colorStateList
    }

    private fun unCheckButtons() {
        binding.answersRadioGroup.clearCheck()
    }

    private fun nextQuestion() {
        currentQuiz = quizzes[questionIndex]
        setQuestion()
        binding.invalidateAll()
    }

    private fun updateQuizProgress() {
        binding.progressBar.incrementProgressBy((1.0 / quizzes.size.toFloat() * 100.00).toInt())
    }

    private fun heartsNum(correct: Boolean) {
        if (!correct) hearts -= 1 //else hearts += 1
        sharedPref.edit().putString("hearts", hearts.toString()).apply()
        binding.heartsNum.text = hearts.toString()
    }

    private fun openNextStage(correctAnswers: Int) {
        val nextStage = viewModel.nextStage.value
        if ((correctAnswers.toFloat() / quizzes.size * 100).toInt() >= 65) {
            viewModel.updateBadgeAchievement()
            nextStage?.passed = true
            AqsaLandmarksApplication.calculateStreak(true)
        }
        else{
            AqsaLandmarksApplication.calculateStreak(false)
        }
        viewModel.updateStage(viewModel.nextStage.value!!)
    }

    private fun setScore(correctAnswers: Int): Int {
        return when ((correctAnswers.toFloat() / quizzes.size * 100).toInt()) {
            0 -> 0
            in 75..100 -> 3
            in 65..74 -> 2
            else -> 1
        }
    }

    private fun updateStageScore(score: Int) {
        viewModel.stage.value?.score = score
        viewModel.updateStage(viewModel.stage.value!!)
    }

    private fun disableChoose() {
        binding.answer1RadioButton.isEnabled = false
        binding.answer2RadioButton2.isEnabled = false
        binding.answer3RadioButton3.isEnabled = false
    }

    private fun enableChoose() {
        binding.answer1RadioButton.isEnabled = true
        binding.answer2RadioButton2.isEnabled = true
        binding.answer3RadioButton3.isEnabled = true
    }
}