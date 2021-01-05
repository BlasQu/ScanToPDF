package com.example.scantopdf.ROOM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scantopdf.Data.Doc

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(data: Doc)

    @Query("SELECT * FROM doc_data WHERE title LIKE '%' || :search || '%' ORDER BY id ASC")
    fun searchMediatorData(search: String) : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data WHERE title LIKE '%' || :search || '%' ORDER BY id DESC")
    fun searchMediatorDataDESC(search: String) : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data WHERE title LIKE '%' || :search || '%' ORDER BY title ASC")
    fun searchMediatorDataTitleASC(search: String) : LiveData<List<Doc>>

    @Query("SELECT * FROM doc_data WHERE title LIKE '%' || :search || '%' ORDER BY title DESC")
    fun searchMediatorDataTitleDESC(search: String) : LiveData<List<Doc>>

    @Query("DELETE FROM doc_data WHERE date = :date")
    suspend fun deleteData(date: String) // The better way would be to delete with id, however date is also unique for each one and for id there should be another setter in vm
}