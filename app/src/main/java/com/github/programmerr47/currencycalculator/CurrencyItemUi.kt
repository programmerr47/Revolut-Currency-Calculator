package com.github.programmerr47.currencycalculator

import android.view.Gravity.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.*
import android.widget.LinearLayout.*
import org.jetbrains.anko.*

class CurrencyItemUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            orientation = HORIZONTAL

            textView {
                id = R.id.tv_type
                layoutParams = LayoutParams(wrapContent, wrapContent).apply {
                    marginStart = dimenInt(R.dimen.padding_def)
                    topMargin = dimenInt(R.dimen.padding_def)
                    bottomMargin = dimenInt(R.dimen.padding_def)
                    marginEnd = dimenInt(R.dimen.padding_small)
                    gravity = CENTER
                }
                textSizePx = dimen(R.dimen.size_text_def)
            }

            frameLayout {
                lparams(matchParent, wrapContent) {
                    marginStart = dimenInt(R.dimen.padding_small)
                    topMargin = dimenInt(R.dimen.padding_def)
                    bottomMargin = dimenInt(R.dimen.padding_def)
                    marginEnd = dimenInt(R.dimen.padding_def)
                }

                editText {
                    id = R.id.et_value
                    layoutParams = LayoutParams(wrapContent, wrapContent).apply {
                        gravity = CENTER_VERTICAL and END
                    }
                    textSizePx = dimen(R.dimen.size_text_def)
                    imeOptions = IME_ACTION_DONE
                    setRawInputType(TYPE_CLASS_NUMBER and TYPE_NUMBER_FLAG_DECIMAL)
                }
            }
        }
    }
}