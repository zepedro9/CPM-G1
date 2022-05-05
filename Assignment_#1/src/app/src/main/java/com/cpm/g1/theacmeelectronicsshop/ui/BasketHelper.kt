package com.cpm.g1.theacmeelectronicsshop.ui

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
const val DATABASE_NAME = "basket"
const val SCHEMA_VERSION = 15

class BasketHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Product( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "brand TEXT NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "price FLOAT NOT NULL, " +
                    "quantity INTEGER DEFAULT 1, " +
                    "image_url String NOT NULL, " +
                    "date TEXT DEFAULT CURRENT_TIMESTAMP)")
        }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS Product")
        onCreate(db)
    }

    fun insert(id: String, name: String, brand: String, description: String, price: Float, imageUrl: String): Long {
        val cv = ContentValues()
        cv.put("_id", id)
        cv.put("name", name)
        cv.put("brand", brand)
        cv.put("description", description)
        cv.put("price", price)
        cv.put("image_url", imageUrl)
        return writableDatabase.insert("Product", "_id", cv)
    }

    fun updateQuantity(id: String, quantity: Int): Int {
        val cv = ContentValues()
        val args = arrayOf(id)
        cv.put("quantity", quantity)
        return writableDatabase.update("Product", cv, "_id = ?", args)
    }

    fun deleteById(id: String) {
        val args = arrayOf(id)
        writableDatabase.delete("Product", "_id = ?", args)
    }

    fun getById(id: String) : Cursor {
        val args = arrayOf(id)
        return readableDatabase.rawQuery(
            "SELECT _id, name, brand, description, price, quantity, image_url FROM Product " +
                    "WHERE _id = ?", args)
    }

    fun getAll(): Cursor {
        return readableDatabase.rawQuery(
            "SELECT _id, name, brand, description, price, quantity, image_url " +
                    "FROM Product ORDER BY date DESC",
            null
        )
    }

    fun getBasketProducts(): Cursor {
        return readableDatabase.rawQuery(
            "SELECT _id, quantity FROM Product",
            null
        )
    }

    fun getBasketTotal() : Float {
        val cursor = readableDatabase.rawQuery(
            "SELECT SUM(quantity*price) " +
                "FROM Product",
            null
        )

        if(cursor.moveToFirst()){
            return cursor.getFloat(0)
        }

        return 0F
    }

    fun getId(c: Cursor): String {
        return c.getString(0)
    }

    fun getName(c: Cursor): String {
        return c.getString(1)
    }

    fun getBrand(c: Cursor): String {
        return c.getString(2)
    }

    fun getDescription(c: Cursor): String {
        return c.getString(3)
    }

    fun getPrice(c: Cursor): Float {
        return c.getFloat(4)
    }

    fun getQuantity(c: Cursor): Int {
        return c.getInt(5)
    }

    fun getImageUrl(c: Cursor): String {
        return c.getString(6)
    }

}