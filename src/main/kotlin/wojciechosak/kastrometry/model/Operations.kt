package wojciechosak.kastrometry.model

import kotlin.math.pow

fun distance(star1: Star, star2: Star): Double {
    return kotlin.math.sqrt((star1.x - star2.x).pow(2.0) + (star1.y - star2.y).pow(2.0))
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()