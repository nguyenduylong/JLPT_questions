package com.tokui.tokuijlptquestions.repository

import com.tokui.tokuijlptquestions.dao.QuestionTypeDao

class QuestionTypeRepository(private  val questionTypeDao: QuestionTypeDao) {
    fun getIdFromTypeAndLevel(type: Int, level: Int): Int {
        val id: Int = questionTypeDao.getQuestionTypeID(type, level)
        return id
    }
}