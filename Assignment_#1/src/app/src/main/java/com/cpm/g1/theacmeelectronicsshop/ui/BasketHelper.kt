package com.cpm.g1.theacmeelectronicsshop.ui

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
const val DATABASE_NAME = "basket"
const val SCHEMA_VERSION = 22

class BasketHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            // Basket Table
            db.execSQL("CREATE TABLE BasketItem(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "product_id LONG NOT NULL,"+
                    "user_id TEXT NOT NULL,"+
                    "quantity INTEGER DEFAULT 1, " +
                    "date TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "UNIQUE(product_id, user_id))")
        }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS BasketItem")
        onCreate(db)
    }

    fun getBasket(userId: String): Cursor {
        val args = arrayOf(userId)
        return readableDatabase.rawQuery(
            "SELECT product_id, quantity " +
                    "FROM BasketItem " +
                    "WHERE user_id = ? " +
                    "ORDER BY date DESC",
            args
        )
    }

    fun clearBasket(userId: String){
        val args = arrayOf(userId)
        writableDatabase.delete("BasketItem", "user_id = ?", args)
    }

    fun getBasketItemById(userId: String, productId: String) : Cursor {
        val args = arrayOf(userId, productId)
        return readableDatabase.rawQuery(
            "SELECT product_id, quantity FROM BasketItem " +
                    "WHERE user_id = ? AND product_id = ?", args)
    }

    fun insertBasketItem(userId:String, productId: String): Long {
        val cv = ContentValues()
        cv.put("product_id", productId)
        cv.put("user_id", userId)
        return writableDatabase.insert("BasketItem", "product_id", cv)
    }

    fun updateItemQuantity(userId: String, productId: String, quantity: Int): Int {
        val cv = ContentValues()
        val args = arrayOf(userId, productId)
        cv.put("quantity", quantity)
        return writableDatabase.update("BasketItem", cv, "user_id = ? AND product_id = ?", args)
    }

    fun deleteBasketItem(userId: String, productId: String) {
        val args = arrayOf(userId, productId)
        writableDatabase.delete("BasketItem", "user_id = ? AND product_id=?", args)
    }

}