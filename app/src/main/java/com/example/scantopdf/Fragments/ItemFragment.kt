package com.example.scantopdf.Fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.scantopdf.Functions
import com.example.scantopdf.MainActivity
import com.example.scantopdf.R
import kotlinx.android.synthetic.main.fragment_item.*
import kotlinx.android.synthetic.main.progress_bar.*
import java.io.File
import java.io.FileOutputStream

class ItemFragment : Fragment(R.layout.fragment_item) {

    private lateinit var actv: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actv = activity as MainActivity

        btnListeners()

        image_item.setImageBitmap(Bitmap.createScaledBitmap(actv.viewmodel.image, 150, 150, false))
        text_title.text = actv.viewmodel.title
        text_date.text = actv.viewmodel.date
    }

    fun btnListeners() {
        image_item.setOnClickListener {
            openPDF()
        }
        btn_deleteitem.setOnClickListener {
            Functions().dialogConfirm(actv)
        }
        btn_scantopdf.setOnClickListener {
            val pdf = PdfDocument()
            val pi = PdfDocument.PageInfo.Builder(
                actv.viewmodel.fullResImage.width,
                actv.viewmodel.fullResImage.height,
                1
            ).create()
            val page = pdf.startPage(pi)
            val canvas = page.canvas
            val paint = Paint()
            paint.color = Color.WHITE
            canvas.drawPaint(paint)
            paint.color = Color.BLUE
            canvas.drawBitmap(actv.viewmodel.fullResImage, 0f, 0f, null)
            pdf.finishPage(page)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                val folder = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/ScanToPDF/")
                folder.mkdirs()
                val file = File(folder, actv.viewmodel.title + ".pdf")
                file.createNewFile()
                val o = FileOutputStream(file)
                pdf.writeTo(o)
                pdf.close()
                if(folder.exists()) Toast.makeText(actv, "File was succesfully created! Click on image to preview document.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openPDF() {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/ScanToPDF/" + actv.viewmodel.title + ".pdf")
        val target = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(actv, actv.applicationContext.packageName + ".provider", file)
        target.setDataAndType(uri, "application/pdf")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        if (file.exists()) startActivity(Intent.createChooser(target, "Open File"))
        else Toast.makeText(actv, "File is nowhere to be found! Try to create it first!", Toast.LENGTH_SHORT).show()
    }

}