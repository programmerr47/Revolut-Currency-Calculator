package com.github.programmerr47.currencycalculator.currencylist

import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.END
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.TYPE_CLASS_NUMBER
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import com.github.programmerr47.currencycalculator.R
import com.github.programmerr47.currencycalculator.util.dimen
import com.github.programmerr47.currencycalculator.util.dimenInt
import com.github.programmerr47.currencycalculator.util.textSizePx
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.editText
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

object CurrencyItemUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            orientation = HORIZONTAL

            textView {
                id = R.id.tv_type
                layoutParams = LinearLayout.LayoutParams(wrapContent, wrapContent).apply {
                    marginStart = dimenInt(R.dimen.padding_def)
                    topMargin = dimenInt(R.dimen.padding_small)
                    bottomMargin = dimenInt(R.dimen.padding_small)
                    marginEnd = dimenInt(R.dimen.padding_small)
                    gravity = CENTER
                }
                textSizePx = dimen(R.dimen.size_text_def)
            }

            frameLayout {
                lparams(matchParent, wrapContent) {
                    marginStart = dimenInt(R.dimen.padding_small)
                    topMargin = dimenInt(R.dimen.padding_small)
                    bottomMargin = dimenInt(R.dimen.padding_small)
                    marginEnd = dimenInt(R.dimen.padding_def)
                }

                editText {
                    id = R.id.et_value
                    layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent).apply {
                        gravity = CENTER_VERTICAL or END
                    }
                    textSizePx = dimen(R.dimen.size_text_def)
                    imeOptions = IME_ACTION_DONE
                    setRawInputType(TYPE_CLASS_NUMBER)
                }
            }
        }
    }
}
