package wojciechosak.kastrometry.comparator

import com.marcinmoskala.math.permutations
import wojciechosak.kastrometry.config.GlobalConfiguration.PRECISION
import wojciechosak.kastrometry.model.Star
import wojciechosak.kastrometry.model.StarsGroup
import wojciechosak.kastrometry.model.distance
import wojciechosak.kastrometry.model.round
import java.lang.Math.toDegrees
import kotlin.math.absoluteValue

object StarsGroupComparator {

    fun compare(group1: StarsGroup, group2: StarsGroup): Double {
        val group1DistanceRatio =
            calculateStarsDistanceRatio(
                group1
            )
        val group2DistanceRatio =
            calculateStarsDistanceRatio(
                group2
            )
        val distanceRatioSimilarityList = arrayListOf<Double>()
        group1DistanceRatio.forEachIndexed { index, value ->
            distanceRatioSimilarityList.add(
                percentDifference(
                    value,
                    group2DistanceRatio[index]
                )
            )
        }
        val distanceRatioResult = distanceRatioSimilarityList.average()

        val group1InnerAngles =
            calculateInnerAnglesRatio(
                group1
            )
        val group2InnerAngles =
            calculateInnerAnglesRatio(
                group2
            )
        val innerAnglesSimilarityList = arrayListOf<Double>()
        group1InnerAngles.forEachIndexed { index, value ->
            innerAnglesSimilarityList.add(
                percentDifference(
                    value,
                    group2InnerAngles[index]
                )
            )
        }
        val innerAnglesResult = innerAnglesSimilarityList.average()

        return arrayListOf(innerAnglesResult, distanceRatioResult).average()
    }

    private fun calculateStarsDistanceRatio(group: StarsGroup): List<Double> {
        val lengths = group.asCompleteGraphPairs().map {
            distance(it.first, it.second)
        }.toList().sorted()

        val shortestLength = lengths.first { it > 0.0 }
        return lengths.drop(1).map {
            it.div(shortestLength)
        }.sorted()
    }

    fun calculateInnerAnglesRatio(group: StarsGroup): List<Double> {
        val angles = arrayListOf<Double>()
        group.asCompleteGraphTriples().map { triple ->
            val permutationsList =
                arrayListOf<List<Star>>() // contains all of permutations without permutations that calculate same angle.
            // for example a->b->c angle is the same like c->b->a angle, to avoid such repeatitions we use that list to filter them out.

            for (permutation in triple.toList().permutations()) {
                val a = permutation[0]
                val b = permutation[1]
                val c = permutation[2]
                permutationsList.firstOrNull { (it[0] == a || it[0] == c) && it[1] == b && (it[2] == a || it[2] == c) }
                    .let {
                        if (it == null) {
                            permutationsList.add(permutation)
                        }
                    }
            }
            permutationsList.forEach { permutation ->
                println(
                    "${permutation[0]},${permutation[1]},${permutation[2]}, angle: ${
                        calculateAngle(
                            permutation[0],
                            permutation[1],
                            permutation[2]
                        )
                    } "
                )
                angles.add(
                    calculateAngle(
                        permutation[0],
                        permutation[1],
                        permutation[2]
                    )
                )
            }
        }
        return angles.sorted()
    }

    fun calculateAngle(star1: Star, star2: Star, star3: Star): Double {
        val degrees = toDegrees(
            kotlin.math.atan2(star3.y - star2.y, star3.x - star2.x) -
                    kotlin.math.atan2(star1.y - star2.y, star1.x - star2.x)
        ).absoluteValue
        return if (degrees >= 180.0) {
            (360.0 - degrees)
        } else {
            degrees
        }.round(PRECISION)
    }

    private fun calculateBrightnessRatio() {
        // TODO brightness analysis feature
    }

    private fun percentDifference(number1: Double, number2: Double): Double {
        return when {
            number1 < number2 -> {
                (number1 * 100.0) / number2
            }
            number1 > number2 -> {
                (number2 * 100.0) / number1
            }
            else -> {
                100.0
            }
        }
    }
}