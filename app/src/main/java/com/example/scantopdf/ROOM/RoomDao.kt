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

    @Query("SELECT * FROM doc_data ORDER BY id ASC")
    fun getData() : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data ORDER BY id DESC")
    fun getDataDESC() : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data ORDER BY title ASC")
    fun getDataTitleASC() : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data ORDER BY title DESC")
    fun getDataTitleDESC() : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data WHERE title LIKE '%' || :search || '%' or date LIKE '%' || :search || '%'")
    fun searchData(search: String) : LiveData<List<Doc>>

    @Query("DELETE FROM doc_data WHERE date = :date")
    suspend fun deleteData(date: String) // The better way would be to delete with id, however date is also unique for each one and for id there should be another setter in vm
}