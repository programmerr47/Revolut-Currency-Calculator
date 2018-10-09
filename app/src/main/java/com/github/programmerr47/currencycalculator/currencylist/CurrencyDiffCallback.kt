package com.github.programmerr47.currencycalculator.currencylist

import androidx.recyclerview.widget.DiffUtil

class CurrencyDiffCallback(
        private val old: List<CurrencyItem>,
        private val new: List<CurrencyItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size
    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int) = old[oldPos].type == new[newPos].type
    override fun areContentsTheSame(oldPos: Int, newPos: Int) = old[oldPos].value == new[newPos].value
    override fun getChangePayload(oldPos: Int, newPos: Int) = old[oldPos].value to new[newPos].value
}