package com.example.scantopdf.ROOM

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scantopdf.Data.Doc

@Database(version = 1, entities = [Doc::class])
abstract class RoomDB : RoomDatabase() {

    abstract fun getDao() : RoomDao

    companion object {
        var instance: RoomDB? = null
        fun createDB(context: Context) : RoomDB {
            if (instance != null){
                return instance as RoomDB
            }
            synchronized(this){
                instance = Room.databaseBuilder(context, RoomDB::class.java, "pdf_db").build()
                return instance as RoomDB
            }
        }
    }
}