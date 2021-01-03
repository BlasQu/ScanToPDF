package com.example.scantopdf.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.MainActivity
import com.example.scantopdf.R
import com.example.scantopdf.RecyclerView.Adapter
import kotlinx.android.synthetic.main.fragment_documents.*

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private lateinit var Activity : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Activity = activity as MainActivity // Set reference to MainActivity

        setupRV() // Setup Recycler View
    }

    fun setupRV(){
        val adapter = Adapter()
        rvview_documents.apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        Activity.viewmodel.liveDataDoc.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) adapter.submitData(it)
        }) // Adapter and data observer to viewmodel

    }

}