package com.example.madlevel4task1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 1, exportSchema = false)
abstract class ShoppingItemRoomDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        private const val DATABASE_NAME = "SHOPPING_LIST_DATABASE"

        @Volatile
        private var shoppingItemRoomDatabaseInstance: ShoppingItemRoomDatabase? = null

        fun getDatabase(context: Context): ShoppingItemRoomDatabase? {
            if (shoppingItemRoomDatabaseInstance == null) {
                synchronized(ShoppingItemRoomDatabase::class.java) {
                    if (shoppingItemRoomDatabaseInstance == null) {
                        shoppingItemRoomDatabaseInstance =
                            Room.databaseBuilder(context.applicationContext,ShoppingItemRoomDatabase::class.java, DATABASE_NAME).build()
                    }
                }
            }
            return shoppingItemRoomDatabaseInstance
        }
    }

}
