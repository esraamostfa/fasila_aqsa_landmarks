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
import com.fasila.aqsalandmarks.model.realtime.Stage
import com.fasila.aqsalandmarks.rootRef
import com.google.firebase.auth.FirebaseAuth


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
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var numQuizzes: Int = 0

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

        setQuizCancelButton()

        binding.quiz = this
        quizzes = viewModel.quizzes.value?.toMutableList()!!
        val footers = listOf(15, 21, 29, 46, 60, 64, 74, 90, 95, 100)
        numQuizzes =
            if (viewModel.stage.value?.id?.toInt() in footers) quizzes.size.coerceAtMost(13) else quizzes.size.coerceAtMost(
                3
            )
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
        val cardId = currentQuiz.cardImageId
        val card = viewModel.getCard(cardId)
        val imageResource = when (currentQuiz.id) {
            "90" -> R.drawable.qebly_marwany
            "86" -> R.drawable.gamea_alnesaa2
            "82" -> R.drawable.gamea_alnesaa2
            else -> activity?.resources?.getIdentifier(card.uri, null, activity?.packageName)
        }
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
        setQuestionImage()
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
        if (quizzes.size < numQuizzes) {
            binding.progressBar.incrementProgressBy((1.0 / quizzes.size.toFloat() * 100.00).toInt())
        } else {
            binding.progressBar.incrementProgressBy((1.0 / numQuizzes.toFloat() * 100.00).toInt())
        }
    }

    private fun heartsNum(correct: Boolean) {
        if (!correct) hearts -= 1 //else hearts += 1
        sharedPref.edit().putString("hearts", hearts.toString()).apply()
        currentUser?.let {
            rootRef.child(it.uid).child("hearts").setValue(hearts.toString())
        }
        binding.heartsNum.text = hearts.toString()
    }

    private fun openNextStage(correctAnswers: Int) {
        val nextStage = viewModel.nextStage.value!!
        val realtimeNextStage = Stage(nextStage.id, nextStage.passed, 0)
        if ((correctAnswers.toFloat() / quizzes.size * 100).toInt() >= 65) {
            viewModel.updateBadgeAchievement(currentUser)
            nextStage.passed = true
            currentUser?.let {
                rootRef.child(it.uid).child("stages").child(nextStage.id)
                    .setValue(Stage(nextStage.id, true, 0))
            }
            AqsaLandmarksApplication.calculateStreak(true)
            val streak = sharedPref.getInt("streakCounter", 0)
            val lastDay = sharedPref.getInt("lastDay", 0)
            currentUser?.let {
                rootRef.child(it.uid).child("streak").setValue(streak)
                rootRef.child(it.uid).child("lastDay").setValue(lastDay)
            }
        } else {
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
        val stage = viewModel.stage.value!!
        val realtimeStage = Stage(stage.id, stage.passed, score)
        currentUser?.let {
            rootRef.child(it.uid).child("stages").child(stage.id).setValue(realtimeStage)
        }
        //stageRef.addChildEventListener(Object ValueEventListener() {})

        viewModel.stage.value?.score = realtimeStage.score
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