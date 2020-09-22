package com.tokui.tokuijlptquestions.repository

import androidx.lifecycle.LiveData
import com.tokui.tokuijlptquestions.dao.QuestionDao
import com.tokui.tokuijlptquestions.model.Question

class QuestionRepository(private  val questionDao: QuestionDao) {

    fun getIdsFromQuestionType(questionType: Int): LiveData<List<Int>> {
        val ids: LiveData<List<Int>> = questionDao.getIdsFromQuestionType(questionType)

        return ids
    }

    fun getQuestionsFromIds(ids: List<Int>): LiveData<List<Question>> {
        val questions: LiveData<List<Question>> = questionDao.getQuestionFromIds(ids)

        return questions
    }

    fun getQuestionsByQuestionType(questionType: Int): LiveData<List<Question>> {
        val questions: LiveData<List<Question>> = questionDao.getQuestionsByQuestionType(questionType)

        return questions
    }
}