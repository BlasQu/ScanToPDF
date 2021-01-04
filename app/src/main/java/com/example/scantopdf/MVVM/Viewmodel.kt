package com.example.scantopdf.MVVM

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.ROOM.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Viewmodel(app: Application) : AndroidViewModel(app) {

    val numberSort = MutableLiveData<Int>((app.getSharedPreferences("ScanToPDF", Context.MODE_PRIVATE).getInt("sortBy", 0)))
    val searchString = MutableLiveData<String>("")
    private val repo : Repository
    var liveDataDoc : LiveData<List<Doc>>
    private var mediatorData = MediatorLiveData<Pair<MutableLiveData<String>, MutableLiveData<Int>>>().apply {
        addSource(searchString) {
            postValue(Pair(searchString, numberSort))
        }
        addSource(numberSort) {
            postValue(Pair(searchString, numberSort))
        }
    }

    lateinit var image : Bitmap
    lateinit var title : String
    lateinit var date : String

    init {
        val dao = RoomDB.createDB(app).getDao()
        repo = Repository(dao)
        liveDataDoc = Transformations.switchMap(mediatorData) {
            repo.readMediatorData(it.first.value!!, it.second.value!!)
        }
    }

    fun insertData(data: Doc) {
        viewModelScope.launch(Dispatchers.IO){
            repo.insertData(data)
        }
    }

    fun deleteData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteData(date)
        }
    }
}