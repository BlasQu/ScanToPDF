package com.example.scantopdf.RecyclerView

import android.graphics.Bitmap
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scantopdf.Data.Doc
import com.example.scantopdf.Fragments.DocumentsFragment
import com.example.scantopdf.Fragments.ItemFragment
import com.example.scantopdf.MainActivity
import com.example.scantopdf.R
import kotlinx.android.synthetic.main.rv_item.view.*

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val list = mutableListOf<Doc>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            val context = itemView.context as MainActivity
            itemView.setOnClickListener {
                synchronized(this) {
                    context.viewmodel.image = list[adapterPosition].image
                    context.viewmodel.title = list[adapterPosition].title
                    context.viewmodel.date = list[adapterPosition].date
                }
                context.supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(R.anim.slide_from_right_enter, R.anim.slide_from_right_exit)
                    replace(R.id.fragmentContainer, ItemFragment(), "ITEM_FRAGMENT")
                    commit()
                }
                context.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }
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
            return (oldList[oldItemPosition].date == newList[newItemPosition].date)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.text_title.text = list[position].title
        holder.itemView.text_date.text = list[position].date
        holder.itemView.img_preview.setImageBitmap(Bitmap.createScaledBitmap(list[position].image, 100, 100, false))
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