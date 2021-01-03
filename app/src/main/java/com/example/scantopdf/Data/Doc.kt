package com.example.scantopdf.Data

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doc_data")
data class Doc(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,@ColumnInfo(name = "title") val title: String, @ColumnInfo(name = "image") val image: Bitmap, @ColumnInfo(name = "date") val date: String)