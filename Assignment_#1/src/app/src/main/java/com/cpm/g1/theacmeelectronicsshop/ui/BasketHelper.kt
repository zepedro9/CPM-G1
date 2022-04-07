package com.cpm.g1.theacmeelectronicsshop.ui

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME = "basket"
const val SCHEMA_VERSION = 1

class BasketHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Product(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, brand TEXT, description TEXT, price FLOAT)")
        }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insert(name: String, brand: String, description: String, price: Float): Long {
        val cv = ContentValues()
        cv.put("name", name)
        cv.put("brand", brand)
        cv.put("type", description)
        cv.put("notes", price)
        return writableDatabase.insert("Product", "name", cv)
    }

    fun update(id: String, name: String, brand: String, description: String, price: Float): Int {
        val cv = ContentValues()
        val args = arrayOf(id)
        cv.put("name", name)
        cv.put("brand", brand)
        cv.put("description", description)
        cv.put("price", price)
        return writableDatabase.update("Product", cv, "_id = ?", args)
    }

    fun getAll(): Cursor {
        return readableDatabase.rawQuery(
            "SELECT _id, name, brand, description, price " +
                    "FROM Restaurants ORDER BY name",
            null
        )
    }

}