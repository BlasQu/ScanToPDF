package com.example.scantopdf.MVVM

import androidx.lifecycle.LiveData
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.ROOM.RoomDao

class Repository(val dao: RoomDao) {
    fun getData() : LiveData<List<Doc>> = dao.getData()

    suspend fun insertData(data: Doc) = dao.insertData(data)
}