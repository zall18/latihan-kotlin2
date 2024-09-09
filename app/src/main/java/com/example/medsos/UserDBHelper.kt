package com.example.medsos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object{
        private val DB_NAME = "medsos"
        private val DB_VERSION = 1
        private val TABLE_NAME = "users"
        private val ID = "id"
        private val NAME = "name"
        private val USERNAME = "username"
        private val EMAIL = "email"
        private val PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var create_table = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $USERNAME TEXT, $EMAIL TEXT, $PASSWORD TEXT)";
        db?.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var drop_table = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(drop_table)
        onCreate(db)
    }

    fun registrationUser(users: UserModel){
        var db = writableDatabase
        val values = ContentValues().apply {
            put(NAME, users.name)
            put(USERNAME, users.username)
            put(EMAIL, users.email)
            put(PASSWORD, users.password)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun login(username: String, password: String): Boolean{
        var db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $USERNAME = ? AND $PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        if(cursor.count > 0){
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
        }
    }

}