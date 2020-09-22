package com.tokui.tokuijlptquestions.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DatabaseCopier() {
    val roomDatabase: TokuiDatabase

    private object Holder {
        val INSTANCE = DatabaseCopier()
    }

    private fun copyAttachedDatabase(
        context: Context?,
        databaseName: String
    ) {
        val dbPath = context!!.getDatabasePath(databaseName)

        // If the database already exists, return
        if (dbPath.exists()) {
            return
        }

        // Make sure we have a path to the file
        dbPath.parentFile.mkdirs()

        // Try to copy database file
        try {
            val inputStream =
                context.assets.open("$databaseName")
            val output: OutputStream = FileOutputStream(dbPath)
            val buffer = ByteArray(8192)
            var length: Int
            while (inputStream.read(buffer, 0, 8192).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }
            output.flush()
            output.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d(TAG, "Failed to open file", e)
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = DatabaseCopier::class.java.simpleName
        private const val DATABASE_NAME = "jlpt_questions.db"
        private var appContext: Context? = null
        fun getInstance(context: Context?): DatabaseCopier {
            appContext = context
            return Holder.INSTANCE
        }
    }

    init {
        //call method that check if database not exists and copy prepopulated file from assets
        copyAttachedDatabase(
            appContext,
            DATABASE_NAME
        )
        roomDatabase = Room.databaseBuilder(
            appContext!!,
            TokuiDatabase::class.java, DATABASE_NAME
        )
            .build()
    }
}