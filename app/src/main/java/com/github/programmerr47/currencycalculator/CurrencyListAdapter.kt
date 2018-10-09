package com.github.programmerr47.currencycalculator

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoContext
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class CurrencyListAdapter(
        private val evaluator: CurrencyEvaluator,
        private val diffFactory: (List<CurrencyItem>, List<CurrencyItem>) -> DiffUtil.Callback = { old, new -> CurrencyDiffCallback(old, new) }
) : RecyclerView.Adapter<CurrencyListAdapter.Holder>() {
    private var list: List<CurrencyItem> = emptyList()
    private val currencyTextWatcher: TextWatcher by lazy { CurrencyWatcher(evaluator) }

    init {
        evaluator.observe()
                .log(object : Logger {
                    override fun log(tag: String, message: String) {
                        Log.v(tag, message)
                    }
                })
                .subscribe({ update(it) }, {
                    Log.v("FUCK", "${Log.getStackTraceString(it.cause)}")
                })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(CurrencyItemUi().createView(AnkoContext.create(parent.context, parent)))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) = with(holder) {
        val item = list[position]
        typeView.text = item.type
        if (!valueView.isFocused) valueView.setText(item.formattedValue)
        valueView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                evaluator.pushOnTop(item.type)
                (v as? EditText)?.addTextChangedListener(currencyTextWatcher)
            } else {
                (v as? EditText)?.removeTextChangedListener(currencyTextWatcher)
            }
        }
    }

    fun update(newList: List<CurrencyItem>) {
        calculateDiff(diffFactory(list, newList))
        list = newList
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeView = bind<TextView>(R.id.tv_type)
        val valueView = bind<EditText>(R.id.et_value)
    }

    class CurrencyWatcher(
            private val evaluator: CurrencyEvaluator
    ) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val bd = if (s.isNullOrBlank()) ZERO else BigDecimal(s.toString())
            evaluator.acceptNew(bd)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }
}