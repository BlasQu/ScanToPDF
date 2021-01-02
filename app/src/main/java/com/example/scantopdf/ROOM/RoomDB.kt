package com.example.scantopdf.ROOM

import android.content.Context
import androidx.room.*
import com.example.scantopdf.Data.Converters
import com.example.scantopdf.Data.Doc

@Database(version = 1, entities = [Doc::class])
@TypeConverters(Converters::class)
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