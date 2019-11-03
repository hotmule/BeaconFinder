package ru.hotmule.beaconfinder.ui.beacon

import android.content.Context
import android.view.View
import android.graphics.*
import android.graphics.Paint.Join
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.findbeacon.R
import ru.hotmule.beaconfinder.utils.dpToPx
import ru.hotmule.beaconfinder.utils.pxToDp
import kotlin.math.abs


class ChartView(internal var context: Context,
                attributeSet: AttributeSet) : View(context, attributeSet) {

    private val path: Path = Path()
    private val paint = Paint()

    private var end = 0f
    private var bottom = 0f
    private var top = 0f
    private var chartTop = 0f
    private var chartBottom = 0f

    var step = 20
    var stepLength = 0

    var data: List<Float> = listOf()
    set(value) {
        field = value
        invalidate()
    }

    init {

        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ChartView,
            0, 0).apply {

            try {
                chartBottom = getFloat(R.styleable.ChartView_bottom, 0f)
                chartTop = getFloat(R.styleable.ChartView_top, 10f)
            } finally {
                recycle()
            }
        }

        paint.color = ContextCompat.getColor(context, R.color.colorRed)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 4f.dpToPx(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        end = w.pxToDp(context)
        top = h.pxToDp(context)
        stepLength = w / step
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.size > 1) {

            var x = end.dpToPx(context)
            var y = isOutOfChart(data[0])

            path.moveTo(x, getProportionalY(y.dpToPx(context)))

            for (i in 1 until data.size) {

                y = isOutOfChart(data[i])
                x -= stepLength
                path.lineTo(x, getProportionalY(y.dpToPx(context)))

                if (i == step)
                    break
            }

            canvas.drawPath(path, paint)
            path.reset()
        }
    }

    private fun isOutOfChart(y: Float): Float {

        if (y > chartTop)
            return chartTop

        if (y < chartBottom)
            return chartBottom

        return y
    }

    private fun getProportionalY(chartY: Float)
        = abs((chartY - chartBottom) / (chartTop - chartBottom) * (top - bottom))
}