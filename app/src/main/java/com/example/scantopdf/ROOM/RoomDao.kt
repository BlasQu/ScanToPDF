package com.example.scantopdf.ROOM

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scantopdf.Data.Doc

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data: Doc)

    @Query("SELECT * FROM doc_data")
    fun getData() : LiveData<List<Doc>>
}