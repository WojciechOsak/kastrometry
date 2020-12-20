package wojciechosak.kastrometry

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wojciechosak.kastrometry.model.Star
import wojciechosak.kastrometry.model.distance
import kotlin.math.sqrt

internal class OperationsKtTest {

    @Test
    fun `same coordinates`() {
        assertEquals(0.0, distance(
            Star(1, 1),
            Star(1, 1)
        )
        )
    }

    @Test
    fun `distance 1`() {
        assertEquals(1.0, distance(
            Star(1, 1),
            Star(2, 1)
        )
        )
        assertEquals(1.0, distance(
            Star(0, 0),
            Star(0, 1)
        )
        )
        assertEquals(1.0, distance(
            Star(1, 0),
            Star(1, 1)
        )
        )
    }

    @Test
    fun `distance sqrt2`() {
        assertEquals(sqrt(2.0), distance(
            Star(1, 1),
            Star(2, 2)
        )
        )
    }
}