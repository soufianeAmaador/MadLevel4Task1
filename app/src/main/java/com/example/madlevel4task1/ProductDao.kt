package com.example.madlevel4task1

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface ProductDao{

    @Query("SELECT * FROM shoppingItemTable")
    suspend fun getAllItems(): List<ShoppingItem>

    @Insert
    suspend fun insertProduct(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteProduct(shoppingItem: ShoppingItem)

    @Query("DELETE FROM shoppingItemTable")
    suspend fun deleteAllProducts()
}