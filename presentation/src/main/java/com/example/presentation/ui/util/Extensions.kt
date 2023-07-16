package com.example.presentation.ui.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import android.widget.Toast as Toast1

fun Fragment.showToast(message: String) {
    Toast1.makeText(requireContext(), message, Toast1.LENGTH_SHORT).show()
}

fun Activity.showToast(message: String) {
    Toast1.makeText(this, message, Toast1.LENGTH_SHORT).show()
}

fun Fragment.color(color: Int) {
    this.requireContext().getColor(color)
}

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.convertDpToPixels(dp: Float) =
    dp * this.resources.displayMetrics.density