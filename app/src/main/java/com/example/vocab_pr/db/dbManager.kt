package com.example.vocab_pr.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class dbManager(val context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null
    fun dbOpen(){
        db = myDbHelper.writableDatabase
    }
    fun dbInsert(word: String, transcription: String, translate: String, wr: String){
        val values = ContentValues().apply{
            put(dbClass.COLUMN_NAME_WORD, word)
            put(dbClass.COLUMN_NAME_TRANSCRIPTION, transcription)
            put(dbClass.COLUMN_NAME_TRANSLATE, translate)
            put(dbClass.COLUMN_NAME_WR, wr)
        }
        db?.insert(dbClass.TABLE_NAME, null, values)
    }
    fun dbRead(dataType: String): ArrayList<String>{
        val dbException: Exception = Exception()
        when(dataType){
            "word" -> {
                val wordList = ArrayList<String>()
                val cursor = db?.query(dbClass.TABLE_NAME, null, null, null, null, null, null)
                while (cursor?.moveToNext()!!){
                    val wordText = cursor.getString(cursor.getColumnIndexOrThrow(dbClass.COLUMN_NAME_WORD))
                    wordList.add(wordText.toString())
                }
                cursor.close()
                return wordList
            }
            "transcription" -> {
                val transcriptionList = ArrayList<String>()
                val cursor = db?.query(dbClass.TABLE_NAME, null, null, null, null, null, null)
                while (cursor?.moveToNext()!!){
                    val transcriptionText = cursor.getString(cursor.getColumnIndexOrThrow(dbClass.COLUMN_NAME_TRANSCRIPTION))
                    transcriptionList.add(transcriptionText.toString())
                }
                cursor.close()
                return transcriptionList
            }
            "translate" -> {
                val translateList = ArrayList<String>()
                val cursor = db?.query(dbClass.TABLE_NAME, null, null, null, null, null, null)
                while (cursor?.moveToNext()!!){
                    val translateText = cursor.getString(cursor.getColumnIndexOrThrow(dbClass.COLUMN_NAME_TRANSLATE))
                    translateList.add(translateText.toString())
                }
                cursor.close()
                return translateList
            }
            "wr" -> {
                val wrList = ArrayList<String>()
                val cursor = db?.query(dbClass.TABLE_NAME, null, null, null, null, null, null)
                while (cursor?.moveToNext()!!){
                    val wrText = cursor.getString(cursor.getColumnIndexOrThrow(dbClass.COLUMN_NAME_WR))
                    wrList.add(wrText.toString())
                }
                cursor.close()
                return wrList
            }
        }
        throw dbException
    }
    fun dbDelete(word: String){
        val selection = "${dbClass.COLUMN_NAME_WORD} LIKE ?"
        val selectionArgs = arrayOf(word)
        val deleteRows = db?.delete(dbClass.TABLE_NAME, selection, selectionArgs)
    }
    fun dbDestroy(){
        dbClose()
        context.deleteDatabase(dbClass.TABLE_NAME)
    }
    fun dbClose(){
        myDbHelper.close()
    }
}