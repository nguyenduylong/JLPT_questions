package com.tokui.tokuijlptquestions.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tokui.tokuijlptquestions.dao.QuestionDao
import com.tokui.tokuijlptquestions.dao.QuestionTypeDao
import com.tokui.tokuijlptquestions.model.Question
import com.tokui.tokuijlptquestions.model.QuestionType

@Database(version = 1, entities = arrayOf(Question::class, QuestionType::class), exportSchema = false)
abstract class TokuiDatabase: RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun questionTypeDao(): QuestionTypeDao
}