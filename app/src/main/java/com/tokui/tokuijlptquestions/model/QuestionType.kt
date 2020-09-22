package com.tokui.tokuijlptquestions.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_types")
class QuestionType (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "level") val level: Int
)