package com.example.scantopdf.MVVM

import androidx.lifecycle.LiveData
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.ROOM.RoomDao

class Repository(val dao: RoomDao) {

    fun getData(number: Int) : LiveData<List<Doc>> {
        return when (number) {
            0 -> dao.getData()
            1 -> dao.getDataDESC()
            2 -> dao.getDataTitleASC()
            3 -> dao.getDataTitleDESC()
            else -> dao.getData()
        }
    }

    suspend fun insertData(data: Doc) = dao.insertData(data)

    suspend fun deleteData(date: String) = dao.deleteData(date)
}