package com.cpm.g1.theacmeelectronicsshop.ui

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
const val DATABASE_NAME = "basket"
const val SCHEMA_VERSION = 6

class BasketHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE Product(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "brand TEXT NOT NULL, description TEXT NOT NULL," +
                    "price FLOAT NOT NULL, quantity INTEGER DEFAULT 0," +
                    "date TEXT DEFAULT CURRENT_TIMESTAMP)")
        }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS Product");
        onCreate(db);
    }

    fun insert(name: String, brand: String, description: String, price: Float, quantity: Int): Long {
        val cv = ContentValues()
        cv.put("name", name)
        cv.put("brand", brand)
        cv.put("description", description)
        cv.put("price", price)
        cv.put("quantity", quantity)
/*        cv.put("barCode", barCode)*/
        return writableDatabase.insert("Product", "name", cv)
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

    fun getAll(): Cursor {
        return readableDatabase.rawQuery(
            "SELECT _id, name, brand, description, price, quantity " +
                    "FROM Product ORDER BY date DESC",
            null
        )
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

    fun getQuantity(c: Cursor): String {
        return c.getInt(5).toString()
    }

}