package com.cpm.g1.theacmeelectronicsshop.ui

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
const val DATABASE_NAME = "basket"
const val SCHEMA_VERSION = 16

class BasketHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            // Basket Table
            db.execSQL("CREATE TABLE BasketItem(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "product_id LONG NOT NULL,"+
                    "user_id TEXT NOT NULL,"+
                    "quantity INTEGER DEFAULT 1, " +
                    "date TEXT DEFAULT CURRENT_TIMESTAMP)")
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

    fun deleteById(id: String) {
        val args = arrayOf(id)
        writableDatabase.delete("Product", "_id = ?", args)
    }

/*    fun getBasketProducts(): Cursor {
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
    }*/
}