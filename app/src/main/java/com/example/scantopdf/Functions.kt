package com.example.scantopdf

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat.getLocales
import com.example.scantopdf.Data.CONSTS
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.Fragments.DocumentsFragment
import kotlinx.android.synthetic.main.dialog_additem.*
import kotlinx.android.synthetic.main.dialog_confirm.*
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*

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

    fun dialogAdd(context: MainActivity, requestCode: Int){
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(false)
            setView(R.layout.dialog_additem)
        }
        val dialog = builder.create()
        dialog.show()

        dialog.btn_additem.setOnClickListener {
            if (dialog.edittext_addtitle.text.isNotEmpty()) {
                when (requestCode) {
                    context.CAMERA_RQ -> context.viewmodel.insertData(Doc(0, dialog.edittext_addtitle.text.toString(), context.dataCamera, SimpleDateFormat("EEEE, yyyy.MM.dd 'at' HH:mm:ss ", getLocales(context.resources.configuration).get(0)).format(Calendar.getInstance().time).toString()))
                    context.IMAGE_RQ -> context.viewmodel.insertData(Doc(0, dialog.edittext_addtitle.text.toString(), context.dataGallery, SimpleDateFormat("EEEE, yyyy.MM.dd 'at' HH:mm:ss ", getLocales(context.resources.configuration).get(0)).format(Calendar.getInstance().time).toString()))
                }
                dialog.dismiss()

            }
            else dialog.edittext_addtitle.error = "Field must not be empty!"
        }

        dialog.btn_cancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun dialogConfirm(context: MainActivity) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(false)
            setView(R.layout.dialog_confirm)
        }
        val dialog = builder.create()
        dialog.show()

        dialog.dialogbtn_confirm.setOnClickListener {
            context.viewmodel.deleteData()
            context.onBackPressed()
            //context.supportFragmentManager.beginTransaction().apply {
            //    replace(R.id.fragmentContainer, DocumentsFragment(), "DOCUMENTS_FRAGMENT")
            //    commit()
            //}
            dialog.dismiss()
        }

        dialog.dialogbtn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun singleChoiceDialog(context: MainActivity) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setSingleChoiceItems(CONSTS.SORTBYITEMS, context.getSharedPrefs()) {dialog, which ->
                dialog.dismiss()
                context.viewmodel.numberSort.postValue(which)
                saveSharedPrefs(context, which) // Save chosen item to shared prefs
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun saveSharedPrefs(context: MainActivity, sortNumber: Int) {
        val prefs = context.getSharedPreferences("ScanToPDF", Context.MODE_PRIVATE).edit()
        prefs.putInt("sortBy", sortNumber)
        prefs.apply()
    }
}