package com.example.scantopdf

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.core.app.ActivityCompat

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
}