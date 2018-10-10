package com.github.programmerr47.currencycalculator.currencylist

import androidx.recyclerview.widget.DiffUtil
import com.github.programmerr47.currencycalculator.db.CurrencyEntity

class CurrencyDiffCallback(
        private val old: List<CurrencyEntity>,
        private val new: List<CurrencyEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int) = old[oldPos].type == new[newPos].type
    override fun areContentsTheSame(oldPos: Int, newPos: Int) = old[oldPos].value == new[newPos].value
    override fun getChangePayload(oldPos: Int, newPos: Int) = old[oldPos].value to new[newPos].value
}