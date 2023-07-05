package com.example.presentation.ui.util

import android.app.Activity
import androidx.fragment.app.Fragment
import android.widget.Toast as Toast1

fun Fragment.showToast(message: String) {
    Toast1.makeText(requireContext(), message, Toast1.LENGTH_SHORT).show()
}

fun Activity.showToast(message: String) {
    Toast1.makeText(this, message, Toast1.LENGTH_SHORT).show()
}

