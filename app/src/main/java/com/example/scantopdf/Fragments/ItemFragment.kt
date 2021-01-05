package com.example.scantopdf.Fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scantopdf.Functions
import com.example.scantopdf.MainActivity
import com.example.scantopdf.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_item.*

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
        btn_deleteitem.setOnClickListener {
            Functions().dialogConfirm(actv)
        }
    }

}