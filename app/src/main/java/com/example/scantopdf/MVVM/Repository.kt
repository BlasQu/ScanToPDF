package com.example.scantopdf.MVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.ROOM.RoomDao

class Repository(val dao: RoomDao) {

    suspend fun insertData(data: Doc) = dao.insertData(data)

    suspend fun deleteData(date: String) = dao.deleteData(date)

    fun readMediatorData(search: String, number: Int) : LiveData<List<Doc>> {
        return when (number) {
            0 -> dao.searchMediatorData(search)
            1 -> dao.searchMediatorDataDESC(search)
            2 -> dao.searchMediatorDataTitleASC(search)
            3 -> dao.searchMediatorDataTitleDESC(search)
            else -> dao.searchMediatorData(search)
        }
    }
}