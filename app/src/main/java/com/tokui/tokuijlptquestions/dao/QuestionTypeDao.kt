package com.tokui.tokuijlptquestions.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.tokui.tokuijlptquestions.model.QuestionType

@Dao
interface QuestionTypeDao {
    @Query("SELECT id FROM question_types WHERE type = :type AND level = :level")
    fun getQuestionTypeID(type: Int, level: Int): Int
    @Query("SELECT * FROM question_types WHERE id in (:ids)")
    fun getQuestionTypeFromIds(ids: List<Int>): LiveData<List<QuestionType>>
}