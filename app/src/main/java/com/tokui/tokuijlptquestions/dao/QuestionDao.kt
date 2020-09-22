package com.tokui.tokuijlptquestions.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.tokui.tokuijlptquestions.model.Question

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions WHERE id in (:ids)")
    fun getQuestionFromIds(ids: List<Int>): LiveData<List<Question>>

    @Query("SELECT id from questions WHERE type = :questionType")
    fun getIdsFromQuestionType(questionType: Int): LiveData<List<Int>>

    @Query("SELECT * from questions WHERE type = :questionType")
    fun getQuestionsByQuestionType(questionType: Int): LiveData<List<Question>>
}