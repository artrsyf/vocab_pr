package com.example.vocab_pr.db

import android.provider.BaseColumns

object dbClass {
    const val TABLE_NAME = "dictionary"
    const val COLUMN_NAME_WORD = "word"
    const val COLUMN_NAME_TRANSCRIPTION = "transcription"
    const val COLUMN_NAME_TRANSLATE = "translate"
    const val COLUMN_NAME_WR = "wr"
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "MyDictionary.db"
    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY,$COLUMN_NAME_WORD TEXT, $COLUMN_NAME_TRANSCRIPTION TEXT, " +
            "$COLUMN_NAME_TRANSLATE TEXT, $COLUMN_NAME_WR TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}