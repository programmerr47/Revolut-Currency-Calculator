package com.github.programmerr47.currencycalculator

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CurrencyListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            Holder(parent.inflate(R.layout.item_currency, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as? TextView)?.text = list[position]
    }

    fun update(newList: List<String>) {
        list = newList
        notifyDataSetChanged() //todo change on DiffUtil
    }

    private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}