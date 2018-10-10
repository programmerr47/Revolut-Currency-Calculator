package com.github.programmerr47.currencycalculator.currencylist

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.programmerr47.currencycalculator.R
import com.github.programmerr47.currencycalculator.db.CurrencyEntity
import com.github.programmerr47.currencycalculator.services.CurrencyEvaluator
import com.github.programmerr47.currencycalculator.services.CurrencyFormatter
import com.github.programmerr47.currencycalculator.util.bind
import com.github.programmerr47.currencycalculator.util.calculateDiff
import com.github.programmerr47.currencycalculator.util.hideKeyboard
import org.jetbrains.anko.AnkoContext
import java.math.BigDecimal

class CurrencyListAdapter(
        private val evaluator: CurrencyEvaluator,
        private val formatter: CurrencyFormatter<BigDecimal>,
        private val diffFactory: (List<CurrencyEntity>, List<CurrencyEntity>) -> DiffUtil.Callback = { old, new -> CurrencyDiffCallback(old, new) }
) : RecyclerView.Adapter<CurrencyListAdapter.Holder>() {
    private var list: List<CurrencyEntity> = emptyList()
    private val currencyTextWatcher: TextWatcher by lazy { CurrencyWatcher(evaluator, formatter) }

    init {
        evaluator.observe().subscribe({ update(it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(CurrencyItemUi().createView(AnkoContext.create(parent.context, parent)))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) = with(holder) {
        val item = list[position]
        typeView.text = item.type
        if (!valueView.isFocused) valueView.setText(formatter.toStr(item.value))
        valueView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                evaluator.setNewType(item.type)
                (v as? EditText)?.addTextChangedListener(currencyTextWatcher)
            } else {
                (v as? EditText)?.removeTextChangedListener(currencyTextWatcher)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        holder.valueView.run { if (isFocused) hideKeyboard() }
    }

    fun update(newList: List<CurrencyEntity>) {
        calculateDiff(diffFactory(list, newList))
        list = newList
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeView = bind<TextView>(R.id.tv_type)
        val valueView = bind<EditText>(R.id.et_value)
    }

    class CurrencyWatcher(
            private val evaluator: CurrencyEvaluator,
            private val formatter: CurrencyFormatter<BigDecimal>
    ) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            evaluator.setNewValue(formatter.fromStr(s.toString()))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}