package com.example.madlevel4task1

import android.content.Context

class ItemRepository (context: Context) {

    private val productDao: ProductDao

    init {
        val database = ShoppingItemRoomDatabase.getDatabase(context)
        productDao = database!!.productDao()
    }

    suspend fun getAllProducts(): List<ShoppingItem> {
        return productDao.getAllItems()
    }

    suspend fun insertProduct(item: ShoppingItem) {
        productDao.insertProduct(item)
    }

    suspend fun deleteProduct(item: ShoppingItem) {
        productDao.deleteProduct(item)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }


}