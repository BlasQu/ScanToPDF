package com.example.scantopdf

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.scantopdf.Data.Doc
import kotlinx.android.synthetic.main.dialog_additem.*

class Functions {

    fun showDialog(context: MainActivity, title: String, description: String){
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(false)
            setTitle(title)
            setMessage(description)
            setPositiveButton("Ok!") { _, _ ->
                ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.CAMERA), context.CAMERA_PERMISSION)
            }
            setNeutralButton("Cancel!") {_, _ -> }
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun dialogAdd(context: MainActivity){
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(false)
            setView(R.layout.dialog_additem)
        }
        val dialog = builder.create()
        dialog.show()

        dialog.btn_additem.setOnClickListener {
            if (dialog.edittext_addtitle.text.isNotEmpty()) {
                context.viewmodel.insertData(Doc(0, dialog.edittext_addtitle.text.toString()))
                dialog.dismiss()
            }
            else dialog.edittext_addtitle.error = "Field must not be empty!"
        }

        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

    }
}