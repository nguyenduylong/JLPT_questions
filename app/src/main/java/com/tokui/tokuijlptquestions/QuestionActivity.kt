package com.tokui.tokuijlptquestions

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokui.tokuijlptquestions.model.Question
import com.tokui.tokuijlptquestions.viewmodel.QuestionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.choice_view.*
import java.text.DecimalFormat

class QuestionActivity : AppCompatActivity(), View.OnClickListener {
    var questionType: Int = 0
    private lateinit var questionViewModel: QuestionViewModel
    private var pickedQuestions: List<Question> = emptyList()
    private var currentQuestion: Int = 0
    private var correctAnswers: IntArray = IntArray(QUESTION_COUNT)
    private var selectedAnswers: IntArray = IntArray(QUESTION_COUNT)
    private lateinit var animFadeIn: Animation
    private var isChecked: Boolean = false
    private var correctCount: Int = 0
    private lateinit var allQuestions: LiveData<List<Question>>
    private var examTime: Int = 10
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var settingPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        var bundle: Bundle ?= intent.extras
        questionType = bundle?.getInt(MainActivity.QUESTION_TYPE)!!

        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)

        allQuestions = questionViewModel.getQuestionsByQuestionType(questionType)

        allQuestions.observe(this, Observer { allQuestions ->
            allQuestions?.let {
                pickedQuestions = allQuestions.shuffled().take(QUESTION_COUNT)
                if (pickedQuestions.isNotEmpty())
                {
                    updateQuestionView()
                    getCorrectAnswers()
                } else {
                    Toast.makeText(this, getString(R.string.no_question), Toast.LENGTH_SHORT).show()
                    finish();
                }
            }
        })

        settingPreferences = getSharedPreferences(SettingActivity.SETTING_REFERENCE, Context.MODE_PRIVATE)
        val settingTime: Int = settingPreferences.getInt(SettingActivity.TIME_SETTING, 1);
        examTime = settingTime * 60

        setRemainTime(examTime);
        countDownTimer = object : CountDownTimer((examTime * 1000).toLong(), 1000) {
            override fun onTick(remain: Long) {
                setRemainTime(Math.floor((remain / 1000).toDouble()).toInt());
            }

            override fun onFinish() {
                if (!isChecked) {
                    check();
                }
            }
        }
        countDownTimer.start()

        next_button.setOnClickListener(this)
        prev_button.setOnClickListener(this)
        choice1_button.setOnClickListener(this)
        choice2_button.setOnClickListener(this)
        choice3_button.setOnClickListener(this)
        choice4_button.setOnClickListener(this)
        check_button.setOnClickListener(this)
        restart_button.setOnClickListener(this)
    }

    override fun onClick(element: View?) {
        when (element) {
            next_button -> nextQuestion()
            prev_button -> prevQuestion()
            check_button -> check()
            choice1_button -> setSelectingChoice(choice1_button, 1)
            choice2_button -> setSelectingChoice(choice2_button, 2)
            choice3_button -> setSelectingChoice(choice3_button, 3)
            choice4_button -> setSelectingChoice(choice4_button, 4)
            restart_button -> newQuiz()
        }
    }

    private fun nextQuestion() {
        if (currentQuestion + 1 < QUESTION_COUNT) {
            currentQuestion += 1
            updateQuestionView()
        }
    }

    private fun prevQuestion() {
        if (currentQuestion != 0) {
            currentQuestion -= 1
            updateQuestionView()
        }
    }

    private fun setSelectingChoice(view: View, selecting_answer: Int) {
        // reset to unselect status
        resetChoiceView()
        // set background for selected view
        view.setBackgroundResource(R.drawable.selecting_choice)
        selectedAnswers[currentQuestion] = selecting_answer
    }

    private fun resetChoiceView() {
        choice1_button.setBackgroundResource(R.drawable.choice_background)
        choice2_button.setBackgroundResource(R.drawable.choice_background)
        choice3_button.setBackgroundResource(R.drawable.choice_background)
        choice4_button.setBackgroundResource(R.drawable.choice_background)
    }

    private fun updateSelectedChoiceView() {
        if (isChecked) {
            //set selected choice
            when (selectedAnswers[currentQuestion]) {
                1 -> choice1_button.setBackgroundResource(R.drawable.incorrected_choice)
                2 -> choice2_button.setBackgroundResource(R.drawable.incorrected_choice)
                3 -> choice3_button.setBackgroundResource(R.drawable.incorrected_choice)
                4 -> choice4_button.setBackgroundResource(R.drawable.incorrected_choice)
            }
            when (correctAnswers[currentQuestion]) {
                1 -> choice1_button.setBackgroundResource(R.drawable.selecting_choice)
                2 -> choice2_button.setBackgroundResource(R.drawable.selecting_choice)
                3 -> choice3_button.setBackgroundResource(R.drawable.selecting_choice)
                4 -> choice4_button.setBackgroundResource(R.drawable.selecting_choice)
            }

        } else {
            // set selected choice
            when (selectedAnswers[currentQuestion]) {
                1 -> choice1_button.setBackgroundResource(R.drawable.selecting_choice)
                2 -> choice2_button.setBackgroundResource(R.drawable.selecting_choice)
                3 -> choice3_button.setBackgroundResource(R.drawable.selecting_choice)
                4 -> choice4_button.setBackgroundResource(R.drawable.selecting_choice)
            }
        }
    }

    private fun updateQuestionView() {
        question_text.text = (currentQuestion + 1).toString() + "）" + pickedQuestions[currentQuestion].question
        choice1_button.text = pickedQuestions[currentQuestion].choice1
        choice2_button.text = pickedQuestions[currentQuestion].choice2
        choice3_button.text = pickedQuestions[currentQuestion].choice3
        choice4_button.text = pickedQuestions[currentQuestion].choice4

        question_text.startAnimation(animFadeIn)
        choice1_button.startAnimation(animFadeIn)
        choice2_button.startAnimation(animFadeIn)
        choice3_button.startAnimation(animFadeIn)
        choice4_button.startAnimation(animFadeIn)

        resetChoiceView()
        updateSelectedChoiceView()
        updateButtonView()
    }

    private fun updateButtonView() {
        if (currentQuestion == 0) {
            prev_button.visibility = View.INVISIBLE
        } else {
            prev_button.visibility = View.VISIBLE
        }

        if (currentQuestion == QUESTION_COUNT - 1) {
            next_button.visibility = View.GONE
            if (isChecked) {
                check_button.visibility = View.GONE
                restart_button.visibility = View.VISIBLE
            } else {
                check_button.visibility = View.VISIBLE
                restart_button.visibility = View.GONE
            }
        } else {
            next_button.visibility = View.VISIBLE
            check_button.visibility = View.GONE
            restart_button.visibility = View.GONE
        }
    }

    private fun getCorrectAnswers() {
        for ((index, question) in pickedQuestions.withIndex()) {
            correctAnswers[index] = question.correctAnswer
        }
    }

    private fun check() {
        for ( (index, value) in selectedAnswers.withIndex()) {
            if (value == correctAnswers[index]) {
                correctCount += 1
            }
        }
        countDownTimer.cancel()
        isChecked = true
        updateButtonView()
        showScoreDialog()
    }

    private fun showScoreDialog() {

        val rewardImageResource: Int = when {
            correctCount >= 8 -> R.drawable.icon_1st
            correctCount in 5..7 -> R.drawable.icon_2nd
            else -> R.drawable.icon_reward_4
        }
        val popupTitleId: Int = when {
            correctCount >= 8 -> R.string.good_result
            else -> R.string.not_good_result
        }

        val dialogView = layoutInflater.inflate(R.layout.popup_score, null)

        val scoreTextView = dialogView.findViewById(R.id.popup_message) as TextView
        val popupTitle = dialogView.findViewById(R.id.popup_title) as TextView
        val okButton = dialogView.findViewById(R.id.ok_button) as TextView
        val rewardImage = dialogView.findViewById(R.id.reward_view) as ImageView

        scoreTextView.text = resources.getString(R.string.quiz_score) + " " + correctCount.toString() + "/" + QUESTION_COUNT.toString()
        popupTitle.text = resources.getString(popupTitleId)
        okButton.text = resources.getString(R.string.ok_button)
        rewardImage.setImageResource(rewardImageResource)


        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        okButton.setOnClickListener {
            customDialog.dismiss()
            updateSelectedChoiceView()
        }

        customDialog.show()
    }

    private fun newQuiz() {
        //reset value
        correctCount = 0
        selectedAnswers = IntArray(QUESTION_COUNT)
        correctAnswers = IntArray(QUESTION_COUNT)
        isChecked = false
        currentQuestion = 0

        allQuestions.observe(this, Observer { allQuestions ->
            allQuestions?.let {
                pickedQuestions = allQuestions.shuffled().take(QUESTION_COUNT)
                if (pickedQuestions.isNotEmpty())
                {
                    updateQuestionView()
                    getCorrectAnswers()
                }
            }
        })
        setRemainTime(examTime);
        countDownTimer.start();


    }

    private fun setRemainTime(remainTimes: Int) {
        val formatter = DecimalFormat("00");
        val remainMinutes = remainTimes / 60;
        val remainSeconds = remainTimes % 60;
        time_counter.text = formatter.format(remainMinutes) + ":" + formatter.format(remainSeconds);
    }

    companion object {
        const val QUESTION_COUNT = 10
    }
}
