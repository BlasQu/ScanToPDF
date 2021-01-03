package com.example.scantopdf.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scantopdf.MainActivity
import com.example.scantopdf.R
import kotlinx.android.synthetic.main.fragment_item.*

class ItemFragment : Fragment(R.layout.fragment_item) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity

        test.text = activity.viewmodel.testText
    }


}