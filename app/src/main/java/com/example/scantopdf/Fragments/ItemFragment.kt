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
import kotlinx.android.synthetic.main.fragment_item.*

class ItemFragment : Fragment(R.layout.fragment_item) {

    private lateinit var Activity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Activity = activity as MainActivity

        btnListeners()

        image_item.setImageBitmap(Bitmap.createScaledBitmap(Activity.viewmodel.image, 150, 150, false))
        text_title.text = Activity.viewmodel.title
        text_date.text = Activity.viewmodel.date
    }

    fun btnListeners() {
        btn_deleteitem.setOnClickListener {
            Functions().dialogConfirm(Activity)
        }
    }

}