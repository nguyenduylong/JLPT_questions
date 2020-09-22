package com.tokui.tokuijlptquestions

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    var currentLevel: Int = 5
    var currentExamTime: Int = 1
    lateinit var settingPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        settingPreferences = getSharedPreferences(SETTING_REFERENCE, Context.MODE_PRIVATE)
        if (settingPreferences.contains(LEVEL_SETTING)) {
            currentLevel = settingPreferences.getInt(LEVEL_SETTING, 5)
        }
        if (settingPreferences.contains(TIME_SETTING)) {
            currentExamTime = settingPreferences.getInt(TIME_SETTING, 1)
        }

        exam_time_input.setText(currentExamTime.toString())

        when(currentLevel) {
            1 -> radio_n1.isChecked = true
            2 -> radio_n2.isChecked = true
            3 -> radio_n3.isChecked = true
            4 -> radio_n4.isChecked = true
            5 -> radio_n5.isChecked = true
        }

        save_button.setOnClickListener(this)
        cancel_button.setOnClickListener(this)

    }

    companion object {
        const val SETTING_REFERENCE = "com.tokui.tokuijlptquestions.setting"
        const val LEVEL_SETTING = "level.setting"
        const val TIME_SETTING = "time.setting"
    }

    override fun onClick(button: View?) {
        when(button) {
            cancel_button -> finish()
            save_button -> saveSetting()
        }
    }

    private  fun  saveSetting() {
        var editor: SharedPreferences.Editor = settingPreferences.edit();

        val examTime: String = exam_time_input.text.toString();
        if (examTime.equals("")) {
            Toast.makeText(this, getString(R.string.time_required), Toast.LENGTH_SHORT).show();
            return
        }
        val checkedId: Int = radio_group_level.checkedRadioButtonId
        if (checkedId == -1) {
            Toast.makeText(this, getString(R.string.level_required), Toast.LENGTH_SHORT).show();
            return
        }
        when(checkedId) {
            R.id.radio_n1 -> currentLevel = 1
            R.id.radio_n2 -> currentLevel = 2
            R.id.radio_n3 -> currentLevel = 3
            R.id.radio_n4 -> currentLevel = 4
            R.id.radio_n5 -> currentLevel = 5
        }
        editor.putInt(LEVEL_SETTING, currentLevel)
        editor.putInt(TIME_SETTING, examTime.toInt());
        editor.commit();
        finish();
    }
}