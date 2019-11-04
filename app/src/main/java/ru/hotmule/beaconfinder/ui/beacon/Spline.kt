package ru.hotmule.beaconfinder.ui.beacon

import java.lang.Float.*
import kotlin.math.hypot


class Spline private constructor(private val xPoints: List<Float>,
                                 private val yPoints: List<Float>,
                                 private val m: FloatArray) {

    companion object {

        fun generate(x: List<Float>?, y: List<Float>?): Spline {
            require(!(x == null || y == null || x.size != y.size || x.size < 2))

            val n = x.size
            val d = FloatArray(n - 1)
            val m = FloatArray(n)

            for (i in 0 until n - 1) {
                val h = x[i + 1] - x[i]
                require(h > 0f)
                d[i] = (y[i + 1] - y[i]) / h
            }

            m[0] = d[0]
            for (i in 1 until n - 1)
                m[i] = (d[i - 1] + d[i]) * 0.5f

            m[n - 1] = d[n - 2]

            for (i in 0 until n - 1) {
                if (d[i] == 0f) {
                    m[i] = 0f
                    m[i + 1] = 0f
                } else {
                    val a = m[i] / d[i]
                    val b = m[i + 1] / d[i]
                    val h = hypot(a.toDouble(), b.toDouble()).toFloat()
                    if (h > 9f) {
                        val t = 3f / h
                        m[i] = t * a * d[i]
                        m[i + 1] = t * b * d[i]
                    }
                }
            }

            return Spline(x, y, m)
        }
    }

    fun interpolate(x: Float): Float {

        val n = xPoints.size
        if (isNaN(x))
            return x

        if (x <= xPoints[0])
            return yPoints[0]

        if (x >= xPoints[n - 1])
            return yPoints[n - 1]


        var i = 0
        while (x >= xPoints[i + 1]) {
            i += 1
            if (x == xPoints[i])
                return yPoints[i]
        }

        val h = xPoints[i + 1] - xPoints[i]
        val t = (x - xPoints[i]) / h
        return (yPoints[i] * (1 + 2 * t) + h * m[i] * t) * (1 - t) * (1 - t) +
                (yPoints[i + 1] * (3 - 2 * t) + h * m[i + 1] * (t - 1)) * t * t
    }
}