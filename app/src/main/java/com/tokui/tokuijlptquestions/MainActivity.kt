package com.tokui.tokuijlptquestions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokui.tokuijlptquestions.viewmodel.QuestionViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var questionType: Int = 0
    private lateinit var settingPreference: SharedPreferences
    private var currentLevel: Int = 5
    private var kanjiQuestionType: Int = 1;
    private var goiQuestionType: Int = 6;
    private var bunpouQuestionType: Int = 11;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_kanji.setOnClickListener(this)
        button_goi.setOnClickListener(this)
        button_bunpou.setOnClickListener(this)
        button_setting.setOnClickListener(this)
        settingPreference = getSharedPreferences(SettingActivity.SETTING_REFERENCE, Context.MODE_PRIVATE);
        if (settingPreference.contains(SettingActivity.LEVEL_SETTING)) {
            currentLevel = settingPreference.getInt(SettingActivity.LEVEL_SETTING, 5)
        }
        kanjiQuestionType = questionTypeMap[currentLevel]?.get("kanji") ?: 1;
        goiQuestionType = questionTypeMap[currentLevel]?.get("goi") ?: 1;
        bunpouQuestionType = questionTypeMap[currentLevel]?.get("bunpou") ?: 1;

        Log.i("goilevel", goiQuestionType.toString())

    }

    override fun onClick(button: View?) {
        when (button) {
            button_kanji -> questionType = kanjiQuestionType
            button_goi -> questionType = goiQuestionType
            button_bunpou -> questionType = bunpouQuestionType
            button_setting -> goToSetting()
        }
        if (questionType != 0) {
            val questionIntent = Intent(this, QuestionActivity::class.java).apply {
                putExtra(QUESTION_TYPE, questionType)
            }
            startActivity(questionIntent)
        }
    }



    private fun goToSetting() {
        val settingIntent = Intent(this, SettingActivity::class.java)
        startActivity(settingIntent)
    }

    override fun onResume() {
        super.onResume()
        if (settingPreference.contains(SettingActivity.LEVEL_SETTING)) {
            currentLevel = settingPreference.getInt(SettingActivity.LEVEL_SETTING, 5)
        }
        questionType = 0;
        kanjiQuestionType = questionTypeMap[currentLevel]?.get("kanji") ?: 1;
        goiQuestionType = questionTypeMap[currentLevel]?.get("goi") ?: 1;
        bunpouQuestionType = questionTypeMap[currentLevel]?.get("bunpou") ?: 1;
    }

    companion object {
        const val QUESTION_TYPE = "question_type"
        val questionTypeMap = hashMapOf<Int, HashMap<String, Int>>(
            1 to hashMapOf<String, Int>(
                "kanji" to 1,
                "goi" to 6,
                "bunpou" to 11
            ),
            2 to hashMapOf<String, Int>(
                "kanji" to 2,
                "goi" to 7,
                "bunpou" to 12
            ),
            3 to hashMapOf<String, Int>(
                "kanji" to 3,
                "goi" to 8,
                "bunpou" to 13
            ),
            4 to hashMapOf<String, Int>(
                "kanji" to 4,
                "goi" to 9,
                "bunpou" to 14
            ),
            5 to hashMapOf<String, Int>(
                "kanji" to 5,
                "goi" to 10,
                "bunpou" to 15
            )
        )

    }
}