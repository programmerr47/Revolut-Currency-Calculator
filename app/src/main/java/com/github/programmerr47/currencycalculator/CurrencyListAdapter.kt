package com.github.programmerr47.currencycalculator

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoContext

class CurrencyListAdapter(
        private val evaluator: CurrencyEvaluator,
        private val diffFactory: (List<CurrencyItem>, List<CurrencyItem>) -> DiffUtil.Callback = { old, new -> CurrencyDiffCallback(old, new) }
) : RecyclerView.Adapter<CurrencyListAdapter.Holder>() {

    private var list: List<CurrencyItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(CurrencyItemUi().createView(AnkoContext.create(parent.context, parent)))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) = with(holder) {
        val item = list[position]
        typeView.text = item.type
        valueView.setText(item.value.toString())
        valueView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                evaluator.pushOnTop(item.type)
            }
        }
    }

    fun update(newList: List<CurrencyItem>) {
        Log.v("FUCK", "OldList: " + list)
        Log.v("FUCK", "NewList: " + newList)
        DiffUtil.calculateDiff(diffFactory(list, newList)).dispatchUpdatesTo(this)
        list = newList
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeView = bind<TextView>(R.id.tv_type)
        val valueView = bind<EditText>(R.id.et_value)
    }
}