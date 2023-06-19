package com.example.todo.util

import androidx.fragment.app.Fragment
import android.widget.Toast as Toast1

fun Fragment.showToast(message: String) {
    Toast1.makeText(requireContext(), message, Toast1.LENGTH_SHORT).show()
}


