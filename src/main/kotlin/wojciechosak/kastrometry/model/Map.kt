package wojciechosak.kastrometry.model

import com.marcinmoskala.math.combinations
import kravis.*
import wojciechosak.kastrometry.comparator.StarsGroupComparator

/**
 * Representation of night sky map.
 * Map can group all stars into smaller groups, each of them will have [groupSize] elements.
 * After grouping map is ready to find specific stars pattern with [find] method.
 *
 * @param stars - list of stars. It is loaded from stars sets like Gaia.
 * @param groupSize - size of stars group that will be
 */
class Map(private val stars: List<Star>, private val groupSize: Int = -1) {

    private val groups: MutableList<StarsGroup> = arrayListOf()

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
    fun find(stars: List<Star>): List<Pair<StarsGroup, Double>> {
        val toFound = StarsGroup(stars)
        initGroups(stars.size)
        val result = groups.associateWith {
            StarsGroupComparator.compare(toFound, it)
        }.toList().sortedBy { it.second }
        val withoutRepetitions = arrayListOf<Pair<StarsGroup, Double>>()
        result.forEach { r ->
            withoutRepetitions.map { it.first.stars }.firstOrNull {
                it.containsAll(r.first.stars)
            }.let {
                if (it == null) {
                    withoutRepetitions.add(r)
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
     * Returns [groupSize] stars that are closest to the [parentStar] in [stars] list.
     * Please notice that it can return list with more elements than [groupSize] in case of equal wojciech.osak.distance
     */
    private fun findClosestStars(parentStar: Star, groupSize: Int): List<Star> {
        val allStars = stars
            .map { Pair(distance(parentStar, it), it) }
            .sortedBy { it.first }
        val firstMdistances = stars
            .asSequence()
            .associateBy { distance(parentStar, it) }
            .toList()
            .map { it.first }
            .sorted()
            .take(groupSize)
            .toList()
        return allStars.takeWhile { it.first in firstMdistances }.map { it.second }
    }

    private fun initGroups(groupSize: Int) {
        for (star in stars) {
            val group = findClosestStars(star, groupSize)
            if (group.size > groupSize) {
                group.toSet().combinations(groupSize).forEach { combination ->
                    groups.add(StarsGroup(combination.toList()))
                }
            } else {
                groups.add(StarsGroup(group))
            }
        }
    }
}