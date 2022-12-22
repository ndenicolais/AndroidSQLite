package com.denicks21.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object{
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "user.db"
            private const val TBL_USER = "tbl_user"
            private const val ID = "id"
            private const val NAME = "name"
            private const val CITY = "city"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblUser = ("CREATE TABLE " + TBL_USER + "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + CITY + " TEXT" + ")")
        db?.execSQL(createTblUser)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_USER")
        onCreate(db)
    }

    fun insertUser(user: UserModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, user.id)
        contentValues.put(NAME, user.name)
        contentValues.put(CITY, user.city)

        val success = db.insert(TBL_USER, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllUser(): ArrayList<UserModel> {
        val userList: ArrayList<UserModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_USER"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var city: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                city = cursor.getString(cursor.getColumnIndex("city"))

                val user = UserModel(id = id, name = name, city = city)
                userList.add(user)
            } while (cursor.moveToNext())
        }

        return userList
    }

    fun updateUser(user: UserModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, user.id)
        contentValues.put(NAME, user.name)
        contentValues.put(CITY, user.city)

        val success = db.update(TBL_USER, contentValues, "id=" + user.id, null)
        db.close()
        return success
    }

    fun deleteUserById(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TBL_USER, "id="+id, null)
        db.close()
        return success
    }
}