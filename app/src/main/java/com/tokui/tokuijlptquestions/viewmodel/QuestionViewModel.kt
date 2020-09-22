package com.tokui.tokuijlptquestions.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tokui.tokuijlptquestions.dao.QuestionDao
import com.tokui.tokuijlptquestions.dao.QuestionTypeDao
import com.tokui.tokuijlptquestions.database.DatabaseCopier
import com.tokui.tokuijlptquestions.database.TokuiDatabase
import com.tokui.tokuijlptquestions.model.Question
import com.tokui.tokuijlptquestions.repository.QuestionRepository
import com.tokui.tokuijlptquestions.repository.QuestionTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.Array
import java.util.*

class QuestionViewModel(application: Application): AndroidViewModel(application) {
    private val repository: QuestionRepository
    private val questionTypeRepository: QuestionTypeRepository

    init {
        val databaseCopier: DatabaseCopier = DatabaseCopier.getInstance(application.applicationContext)
        val tokuiDatabase: TokuiDatabase = databaseCopier.roomDatabase
        val questionDao: QuestionDao = tokuiDatabase.questionDao()
        val questionTypeDao: QuestionTypeDao = tokuiDatabase.questionTypeDao()
        repository = QuestionRepository(questionDao)
        questionTypeRepository = QuestionTypeRepository(questionTypeDao)
    }

    fun getQuestionIds(questionType: Int): LiveData<List<Int>> {
        val questionIds: LiveData<List<Int>> = repository.getIdsFromQuestionType(questionType)
        return questionIds;
    }

    fun getQuestionsByIds(ids: List<Int>): LiveData<List<Question>> {
        val questions: LiveData<List<Question>> = repository.getQuestionsFromIds(ids)
        return questions;
    }

    fun getQuestionsByQuestionType(questionType: Int): LiveData<List<Question>> {
        val questions: LiveData<List<Question>> = repository.getQuestionsByQuestionType(questionType)
        return questions;
    }

    fun  getIdFromTypeAndLevel(type: Int, level: Int): Int {
        return questionTypeRepository.getIdFromTypeAndLevel(type, level)
    }
}