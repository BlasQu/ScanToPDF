package com.example.scantopdf.MVVM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.ROOM.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Viewmodel(app: Application) : AndroidViewModel(app) {

    var liveDataDoc : LiveData<List<Doc>>
    val repo : Repository
    lateinit var testText : String

    init {
        val dao = RoomDB.createDB(app).getDao()
        repo = Repository(dao)
        liveDataDoc = repo.getData()
    }

    fun insertData(data: Doc){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertData(data)
        }
    }
}