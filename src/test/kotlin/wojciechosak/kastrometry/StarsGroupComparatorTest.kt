package wojciechosak.kastrometry

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wojciechosak.kastrometry.comparator.StarsGroupComparator
import wojciechosak.kastrometry.model.Star
import wojciechosak.kastrometry.model.StarsGroup
import wojciechosak.kastrometry.model.round

internal class StarsGroupComparatorTest {

    @Test
    fun calculateInnerAnglesRatio() {
        val triangle = hashSetOf(
            Star(1, 15, "a"),
            Star(3, 15, "b"),
            Star(2, 16, "c")
        )
        val group = StarsGroup(triangle)
        val angles = StarsGroupComparator.calculateInnerAnglesRatio(group)
        println(group.asCompleteGraphTriangle())
        println(angles)
        assertEquals(true, angles.containsAll(hashSetOf(45.0, 45.0, 90.0)))
    }

    @Test
    fun `calculateInnerAnglesRatio2`() {
        val triangle = hashSetOf(
            Star(2, 1, "a"),
            Star(7, 1, "b"),
            Star(4.5, 5.33, "c")
        )
        val group = StarsGroup(triangle)
        val angles = StarsGroupComparator.calculateInnerAnglesRatio(group).map {
            it.round(0)
        }
        assertTrue(
            angles.containsAll(hashSetOf(60.0, 60.0, 60.0))
        )
    }

    @Test
    fun `calculateInnerAnglesRatioSquare`() {
        val square = hashSetOf(
            Star(0, 10, "a"),
            Star(0, 0, "b"),
            Star(10, 0, "c"),
            Star(10, 10, "d")
        )
        val group = StarsGroup(square)
        val angles = StarsGroupComparator.calculateInnerAnglesRatio(group)
        group.asCompleteGraphTriangle().forEach {
            println(it.toList().map { it.name })
        }
        println(angles)
        assertEquals(true, angles.containsAll(hashSetOf(90.0, 90.0, 90.0, 90.0))) // it is acceptable
    }

    @Test
    fun `calculateInnerAnglesRatioFigures`() {
        val figure1 = hashSetOf(
            Star(22, 15, "a"),
            Star(22, 17, "b"),
            Star(22, 18, "c"),
            Star(24, 17, "d")
        )
        val figure2 = hashSetOf(
            Star(29, 13, "a2"),
            Star(31, 13, "b2"),
            Star(32, 13, "c2"),
            Star(31, 11, "d2")
        )
        val group1 = StarsGroup(figure1)
        val group2 = StarsGroup(figure2)
        val angles1 = StarsGroupComparator.calculateInnerAnglesRatio(group1)
        val angles2 = StarsGroupComparator.calculateInnerAnglesRatio(group2)
        println(angles1)
        println(angles2)
        assertEquals(true, angles1.containsAll(angles2))
    }

    @Test
    fun `calculateInnerAnglesRatioRectangle`() {
        val square = hashSetOf(
            Star(0, 100, "a"),
            Star(0, 0, "b"),
            Star(10, 0, "c"),
            Star(10, 100, "d")
        )
        val group = StarsGroup(square)
        val angles = StarsGroupComparator.calculateInnerAnglesRatio(group)
        assertEquals(true, angles.containsAll(hashSetOf(90.0, 90.0, 90.0, 90.0))) // it is acceptable
    }

    @Test
    fun `rotationTest`() {
        val group1 = StarsGroup(
            hashSetOf(
                Star(x = 24.0, y = 17.0, name = "d"),
                Star(x = 22.0, y = 18.0, name = "c"),
                Star(x = 22.0, y = 17.0, name = "b")
            )
        )// 63.43494882292201


        val group2 = StarsGroup(
            hashSetOf(
                Star(x = 31.0, y = 11.0, name = "d2"),
                Star(x = 32.0, y = 13.0, name = "c2"),
                Star(x = 31.0, y = 13.0, name = "b2")
            )
        ) //  296.565051177078

        val angle1 = StarsGroupComparator.calculateInnerAnglesRatio(group1).map { it.round(2)  }
        val angle2 = StarsGroupComparator.calculateInnerAnglesRatio(group2).map { it.round(2) }
        println(angle1)
        println(angle2)
    }


    @Test
    fun scaleTest() {
        val triangle = StarsGroup(
            hashSetOf(
                Star(1, 15, "a"),
                Star(3, 15, "b"),
                Star(2, 16, "c")
            )
        )
        val triangle2 = StarsGroup(
            hashSetOf(
                Star(1, 15, "a2"),
                Star(13, 15, "b2"),
                Star(7, 27, "c2")
            )
        )

        // TODO
    }

    @Test
    fun calculateAngle() {
        assertEquals(
            90.0, StarsGroupComparator.calculateAngle(
                Star(0, 1),
                Star(0, 0),
                Star(1, 0)
            )
        )
    }
}