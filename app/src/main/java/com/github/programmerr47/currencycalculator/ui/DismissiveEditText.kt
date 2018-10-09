package com.github.programmerr47.currencycalculator.ui

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_BACK
import android.view.ViewManager
import androidx.appcompat.widget.AppCompatEditText
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView

inline fun ViewManager.dismissiveEditText(init: (@AnkoViewDslMarker DismissiveEditText).() -> Unit) =
        ankoView({ ctx -> DismissiveEditText(ctx) }, 0, init)

open class DismissiveEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KEYCODE_BACK && event.action == ACTION_UP) {
            clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }
}