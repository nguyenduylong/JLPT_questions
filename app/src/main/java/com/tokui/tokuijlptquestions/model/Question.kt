package com.tokui.tokuijlptquestions.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
class Question (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "choice1") val choice1: String,
    @ColumnInfo(name = "choice2") val choice2: String,
    @ColumnInfo(name = "choice3") val choice3: String,
    @ColumnInfo(name = "choice4") val choice4: String,
    @ColumnInfo(name = "correct_answer") val correctAnswer: Int
)