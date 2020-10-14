package com.example.madlevel4task1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoppingItemTable")
public data class ShoppingItem(

    @ColumnInfo(name = "item")
    val item: String,
    @ColumnInfo(name = "amount")
    val amount: Short,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)

