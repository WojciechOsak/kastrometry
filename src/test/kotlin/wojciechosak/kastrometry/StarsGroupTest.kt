package wojciechosak.kastrometry

import com.marcinmoskala.math.permutations
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import wojciechosak.kastrometry.model.Star
import wojciechosak.kastrometry.model.StarsGroup
import wojciechosak.kastrometry.model.containsCompleteGraph

internal class StarsGroupTest {

    @Test
    fun `complete graph`() {
        val stars = hashSetOf(
            Star(1, 1, "A"),
            Star(1, 3, "B"),
            Star(3, 1, "C"),
            Star(3, 3, "D")
        )
        val groups = hashSetOf<StarsGroup>()
        groups.addAll(stars.permutations().map {
            StarsGroup(it.toHashSet())
        })
        groups.forEach { obj1 ->
            groups.forEach { obj2 ->
                assertEquals(true, obj1.asCompleteGraphPairs().containsCompleteGraph(obj2.asCompleteGraphPairs()))
            }
        }
    }

    @Test
    fun `complete graph triples`() {
        val stars = hashSetOf(
            Star(1, 1, "A"),
            Star(1, 3, "B"),
            Star(3, 1, "C"),
            Star(3, 3, "D")
        )
        val groups = hashSetOf<StarsGroup>()
        groups.addAll(stars.permutations().map {
            StarsGroup(it.toHashSet())
        })

        groups.forEach { obj1 ->
            groups.forEach { obj2 ->
                val obj1Permutations = obj1.asCompleteGraphTriangle().flatMap {
                    it.toList().permutations().map { Pair(it.first(), it.last()) }
                }
                val obj2Permutations = obj2.asCompleteGraphTriangle().flatMap {
                    it.toList().permutations().map { Pair(it.first(), it.last()) }
                }
                assertEquals(true, obj1Permutations.containsCompleteGraph(obj2Permutations))
            }
        }

    }
}