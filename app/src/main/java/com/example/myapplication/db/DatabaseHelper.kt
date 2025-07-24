package com.example.myapplication.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myapplication.model.ProductModel
import com.example.myapplication.model.UserModel

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "AppUserProduct.db"

        // Tables
        private const val TABLE_USER = "user"
        private const val TABLE_PRODUCT = "product"

        // User Columns
        private const val COL_USER_ID = "userId"
        private const val COL_NAME = "name"
        private const val COL_EMAIL = "email"
        private const val COL_PASSWORD = "password"
        private const val COL_USERTYPE = "userType"

        // Product Columns
        private const val COL_PRODUCT_ID = "id"
        private const val COL_PRODUCT_NAME = "productName"
        private const val COL_DESCRIPTION = "description"
        private const val COL_QUANTITY = "quantity"
        private const val COL_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_USER (" +
                    "$COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_NAME TEXT, $COL_EMAIL TEXT UNIQUE, $COL_PASSWORD TEXT, $COL_USERTYPE TEXT)"
        )

        db.execSQL(
            "CREATE TABLE $TABLE_PRODUCT (" +
                    "$COL_PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COL_PRODUCT_NAME TEXT, $COL_DESCRIPTION TEXT, $COL_QUANTITY INTEGER, $COL_PRICE DOUBLE, " +
                    "$COL_USER_ID INTEGER, FOREIGN KEY($COL_USER_ID) REFERENCES $TABLE_USER($COL_USER_ID))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    // ----------------------------
    // USER METHODS
    // ----------------------------

    fun registerUser(user: UserModel): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COL_NAME, user.name)
                put(COL_EMAIL, user.email)
                put(COL_PASSWORD, user.password)
                put(COL_USERTYPE, user.userType)
            }
            db.insertOrThrow(TABLE_USER, null, values)
            true
        } catch (e: Exception) {
            Log.e("DBHelper", "Register error: ${e.message}")
            false
        }
    }

    fun loginUser(email: String, password: String): UserModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USER WHERE $COL_EMAIL = ? AND $COL_PASSWORD = ?",
            arrayOf(email, password)
        )

        return if (cursor.moveToFirst()) {
            val user = UserModel(
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD)),
                userType = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERTYPE))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun checkEmailExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USER WHERE $COL_EMAIL = ?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // ----------------------------
    // PRODUCT METHODS
    // ----------------------------

    fun insertProduct(product: ProductModel): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COL_PRODUCT_NAME, product.name)
                put(COL_DESCRIPTION, product.description)
                put(COL_QUANTITY, product.quantity)
                put(COL_PRICE, product.price)
                put(COL_USER_ID, product.userId)
            }
            db.insert(TABLE_PRODUCT, null, values)
            true
        } catch (e: Exception) {
            Log.e("DBHelper", "InsertProduct error: ${e.message}")
            false
        }
    }

    fun getAllProducts(): List<ProductModel> {
        val db = readableDatabase
        val productList = mutableListOf<ProductModel>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCT", null)

        if (cursor.moveToFirst()) {
            do {
                productList.add(
                    ProductModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID)),
                        userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRODUCT_NAME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return productList
    }

    fun getUserProducts(userId: Int): List<ProductModel> {
        val db = readableDatabase
        val productList = mutableListOf<ProductModel>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCT WHERE $COL_USER_ID = ?", arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                productList.add(
                    ProductModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID)),
                        userId = userId,
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRODUCT_NAME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                        quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return productList
    }


    fun getProduct(productId: Int): ProductModel? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PRODUCT WHERE $COL_PRODUCT_ID = ?",
            arrayOf(productId.toString())
        )

        return if (cursor.moveToFirst()) {
            val product = ProductModel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID)),
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRODUCT_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY)),
                price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE))
            )
            cursor.close()
            product
        } else {
            cursor.close()
            null
        }
    }

    fun updateProduct(product: ProductModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PRODUCT_NAME, product.name)
            put(COL_DESCRIPTION, product.description)
            put(COL_QUANTITY, product.quantity)
            put(COL_PRICE, product.price)
        }
        val result = db.update(TABLE_PRODUCT, values, "$COL_PRODUCT_ID = ?", arrayOf(product.id.toString()))
        return result > 0
    }

    fun deleteProduct(productId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_PRODUCT, "$COL_PRODUCT_ID = ?", arrayOf(productId.toString()))
        return result > 0
    }
}