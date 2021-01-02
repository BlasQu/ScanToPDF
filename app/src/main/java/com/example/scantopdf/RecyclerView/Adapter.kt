package com.example.scantopdf.RecyclerView

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.R
import kotlinx.android.synthetic.main.rv_item.view.*

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val list = mutableListOf<Doc>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    inner class DiffCallBack(val oldList: List<Doc>, val newList: List<Doc>) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].title == newList[newItemPosition].title)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.test.text = list[position].title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitData(newList: List<Doc>){
        val CallbackResult = DiffUtil.calculateDiff(DiffCallBack(list, newList))
        list.clear()
        list.addAll(newList)
        CallbackResult.dispatchUpdatesTo(this)
    }
}