package wojciechosak.kastrometry.model

import com.marcinmoskala.math.combinations

/**
 * Representation of stars constellation as complete graph.
 */
data class StarsGroup(val stars: HashSet<Star>) {

    fun asCompleteGraphPairs(): List<StarsLine> {
        return stars.toSet().combinations(2).map {
            Pair(it.first(), it.last())
        }
    }

    fun asCompleteGraphTriples(): List<Triple<Star, Star, Star>> {
        return stars.toSet().combinations(3).map {
            val list = it.toList()
            Triple(list[0], list[1], list[2])
        }
    }
}

typealias StarsLine = Pair<Star, Star>

fun List<StarsLine>.containsCompleteGraph(value: List<StarsLine>): Boolean {
    return this.flatMap { it.toList() }
        .containsAll(value.flatMap { it.toList() }) && value.size == this.size
}