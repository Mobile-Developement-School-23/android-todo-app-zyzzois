package com.example.presentation.ui.screens.main.snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.presentation.R
import com.google.android.material.snackbar.BaseTransientBottomBar

class TimerSnackBar(
    parent: ViewGroup,
    content: TimerSnackBarView
) : BaseTransientBottomBar<TimerSnackBar>(parent, content, content) {


    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {
        fun make(view: View): TimerSnackBar {
            val parent = view as ViewGroup

            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.custom_snackbar_layout,
                parent,
                false
            ) as TimerSnackBarView

            return TimerSnackBar(parent, customView)
        }

        fun TimerSnackBar.setAction(listener: View.OnClickListener) {
            getView().findViewById<TextView>(R.id.tvCancel).setOnClickListener {
                listener.onClick(it)
                dismiss()
            }
        }

    }
}