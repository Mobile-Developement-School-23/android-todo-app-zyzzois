package com.example.presentation.ui.screens.main.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.DisplayMetrics
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import kotlin.math.roundToInt

class SwipeTodoItemCallback(
    private val applicationContext: Context,
    private val onSwipeLeft: (position: Int) -> Unit,
    private val onSwipeRight: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val acceptSwipePaint = Paint().apply {
        color = applicationContext.getColor(R.color.color_light_green)
    }
    private val deleteSwipePaint = Paint().apply {
        color = applicationContext.getColor(R.color.color_light_red)
    }
    private val whitePaint = Paint().apply {
        colorFilter = PorterDuffColorFilter(
            applicationContext.getColor(R.color.color_light_white),
            PorterDuff.Mode.SRC_IN
        )
    }
    private val acceptIcon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_checked)!!.toBitmap()
    private val deleteIcon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_small_delete)!!.toBitmap()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.layoutPosition
        when (direction) {
            ItemTouchHelper.LEFT -> {
                onSwipeLeft(position)
            }
            ItemTouchHelper.RIGHT -> {
                onSwipeRight(position)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            if (dX > 0) {
                c.drawRect(
                    itemView.left.toFloat(), itemView.top.toFloat(), dX,
                    itemView.bottom.toFloat(), acceptSwipePaint
                )
                c.drawBitmap(
                    acceptIcon,
                    itemView.left.toFloat() - convertDpToPx(40) + dX,
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - acceptIcon.height) / 2,
                    whitePaint
                )
            } else {
                c.drawRect(
                    itemView.right.toFloat() + dX, itemView.top.toFloat(),
                    itemView.right.toFloat(), itemView.bottom.toFloat(), deleteSwipePaint
                )
                c.drawBitmap(
                    deleteIcon,
                    itemView.right.toFloat() + convertDpToPx(40) - deleteIcon.width + dX,
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - deleteIcon.height) / 2,
                    whitePaint
                )

            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
    private fun convertDpToPx(dp: Int): Int {
        return (dp * (applicationContext.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}