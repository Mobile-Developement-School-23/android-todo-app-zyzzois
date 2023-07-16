package com.example.presentation.ui.screens.main.snackbar

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.presentation.R
import com.example.presentation.ui.util.convertDpToPixels
import com.example.presentation.ui.util.getColorFromAttr
import com.google.android.material.snackbar.ContentViewCallback

class TimerSnackBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defaultStyleAttr), ContentViewCallback {

    private val animatedProgressBar: ImageView

    init {
        View.inflate(context, R.layout.snackbar_view, this)
        clipToPadding = false
        this.animatedProgressBar = findViewById(R.id.animatedProgressBar)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        val animatedNumbers = animatedProgressBar.drawable as AnimatedVectorDrawable
        val animatedProgressBar = animatedProgressBar.background as ProgressTimer
        animatedNumbers.reset()
        animatedNumbers.start()
        animatedProgressBar.setColor(context.getColorFromAttr(com.google.android.material.R.attr.colorOnSurface))
        animatedProgressBar.setSize(
            context.convertDpToPixels(60f),
            context.convertDpToPixels(10f)
        )
        animatedProgressBar.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        val animatedProgressBar = animatedProgressBar.background as ProgressTimer
        animatedProgressBar.reset()
    }

}