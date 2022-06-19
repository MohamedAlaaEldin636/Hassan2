package com.maproductions.mohamedalaa.shared.core.extensions

import java.math.BigDecimal

fun Float.toIntOrFloat(): Number = if (this - toInt().toFloat() == 0f) toInt() else this

fun Float.roundHalfUp(scale: Int): Float {
    return toBigDecimal().setScale(scale, BigDecimal.ROUND_HALF_UP).toFloat()
}

fun Float.roundHalfUpToIntOrFloat(scale: Int): Number = roundHalfUp(scale).toIntOrFloat()
