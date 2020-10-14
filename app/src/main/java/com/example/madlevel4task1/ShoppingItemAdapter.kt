package com.example.madlevel4task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class ShoppingItemAdapter(private val items: List<ShoppingItem>) : RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun databind(item: ShoppingItem) {
            itemView.tvAmount.text = item.amount.toString()
            itemView.tvItem.text = item.item.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}