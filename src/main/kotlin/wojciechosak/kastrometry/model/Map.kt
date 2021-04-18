package wojciechosak.kastrometry.model

import com.marcinmoskala.math.combinations
import kravis.*
import wojciechosak.kastrometry.comparator.StarsGroupComparator

/**
 * Representation of sky map.
 * Map can group all stars into smaller groups, each of them will have [groupSize] elements.
 * After grouping map is ready to find specific stars pattern with [find] method.
 * Should be loaded once per program instance, consider to create many instances of Map objects with different groupSize
 * to keep searching with most used group sizes, like 3-20 (most of constellations) and run finding on many threads.
 *
 * @param stars - list of stars. It is loaded from stars sets like Gaia.
 * @param groupSize - size of stars group that will be
 */
class Map(private val stars: List<Star>, private val groupSize: Int = -1) {

    private val groups: HashSet<StarsGroup> = hashSetOf()

    companion object {
        private const val PLOT_TITLE = "Map"
        private const val PLOT_POINT_SHAPE_CODE = 13 // ggplot2 shape code
    }

    /**
     * @param stars - list of stars that we are looking for in the map.
     *
     * @return List<Pair<StarsGroup, Double>> - returns list of pair results. First is group of
     * stars that fits into stars pattern, second is similarity coefficient (0-100.0, where 100.0
     * is 100% similarity).
     */
    fun find(stars: HashSet<Star>): HashSet<MapResult> {
        val toFound = StarsGroup(stars)
        initGroups(stars.size)
        val result = groups.associateWith {
            StarsGroupComparator.compare(toFound, it)
        }
        val withoutRepetitions = hashSetOf<Pair<StarsGroup, Double>>()
        result.forEach { entry ->
            withoutRepetitions.map { it.first.stars }.firstOrNull {
                it.containsAll(entry.key.stars)
            }.let {
                if (it == null) {
                    withoutRepetitions.add(entry.toPair())
                }
            }
        }
        return withoutRepetitions
    }

    /**
     * Plots graphical representation of map on 2D plane with ggplot2.
     */
    fun plot() {
        stars.plot(x = { it.x }, y = { it.y }, label = { it.name })
            .geomPoint(shape = PLOT_POINT_SHAPE_CODE)
            .geomText(hjust = 0.0, vjust = 0.0)
            .title(PLOT_TITLE)
            .show()
    }

    /**
     * Returns [count] stars that are closest to the [parentStar] in [stars] list.
     * Please notice that it can return list with more elements than [count] in case of equal distances.
     */
    private fun findClosestStars(parentStar: Star, count: Int): HashSet<Star> {
        return stars
            .asSequence()
            .map { star -> DistanceFromStar(distance(parentStar, star), star) }
            .sortedBy { it.first }
            .take(count)
            .map { it.second }
            .toHashSet()
    }

    private fun initGroups(groupSize: Int) {
        for (star in stars) {
            val group = findClosestStars(star, groupSize)
            if (group.size > groupSize) {
                group.combinations(groupSize).forEach { combination ->
                    groups.add(StarsGroup(combination.toHashSet()))
                }
            } else {
                groups.add(StarsGroup(group))
            }
        }
    }
}

typealias DistanceFromStar = Pair<Double, Star>

typealias SimilarityCoefficient = Double

typealias MapResult = Pair<StarsGroup, SimilarityCoefficient>