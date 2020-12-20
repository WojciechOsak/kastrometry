package wojciechosak.kastrometry

import wojciechosak.kastrometry.image.ImageProcessor
import wojciechosak.kastrometry.model.Map
import wojciechosak.kastrometry.model.Star
import kotlin.system.measureTimeMillis
import kravis.*

fun main() {
    val skyMap = Map(testDataSet1)
    val searchedShape = testShape2
    //val imageProcessor = ImageProcessor("/img/DSC_0739.JPG")

    measureTimeMillis {
        val result = skyMap.find(searchedShape)
        println("Best fit: ${result.last().first.stars} with similarity coefficient: ${result.last().second}")
    }.also {
        //plotSearchedShape(searchedShape)
        //skyMap.plot()
        println("found in $it ms total")
    }
}

private fun plotSearchedShape(shape: List<Star>) {
    shape.plot(x = { it.x }, y = { it.y }, label = { it.name })
        .geomPoint()
        .title("Searched shape")
        .show()
}

val testDataSet1 = arrayListOf(
    Star(6.0, 29.0, "1"),
    Star(7.0, 13.0, "2"),
    Star(8.0, 21.0, "3"),
    Star(13.0, 38.0, "4"),
    Star(18.0, 34.0, "5"),
    Star(17.0, 25.0, "6"),
    Star(15.0, 23.0, "7"),
    Star(13.0, 21.0, "8"),
    Star(16.0, 12.0, "9"),
    Star(15.0, 11.0, "10"),
    Star(15.0, 7.0, "11"),
    Star(10.0, 0.0, "12"),
    Star(25.0, 29.0, "13"),
    Star(22.0, 18.0, "14"),
    Star(24.0, 17.0, "15"),
    Star(22.0, 17.0, "16"),
    Star(22.0, 15.0, "17"),
    Star(21.0, 6.0, "18"),
    Star(38.0, 37.0, "19"),
    Star(36.0, 35.0, "20"),
    Star(34.0, 33.0, "21"),
    Star(34.0, 25.0, "22"),
    Star(33.0, 23.0, "23"),
    Star(32.0, 22.0, "24"),
    Star(33.0, 20.0, "25"),
    Star(39.0, 17.0, "26"),
    Star(35.0, 8.0, "27"),
    Star(32.0, 6.0, "28"),
    Star(34.0, 4.0, "29"),
    Star(38.0, 4.0, "30"),
    Star(41.0, 39.0, "31"),
    Star(42.0, 35.0, "32"),
    Star(42.0, 23.0, "33"),
    Star(50.0, 20.0, "34"),
    Star(36.0, 5.0, "35"),
    Star(53.0, 33.0, "36"),
    Star(59.0, 21.0, "37"),
    Star(54.0, 15.0, "38"),
    Star(53.0, 14.0, "39"),
    Star(54.0, 12.0, "40")
)

val testShape1 = arrayListOf(
    Star(29.0, 13.0, "B"),
    Star(31.0, 13.0, "C"),
    Star(32.0, 13.0, "E"),
    Star(31.0, 11.0, "D")
)
val testShape2 = arrayListOf(
    Star(4.0, 8.0, "B"),
    Star(6.0, 10.0, "C"),
    Star(6.0, 14.0, "E"),
    Star(5.0, 12.0, "D"),
    Star(2.0, 11.0, "A")
)
val testShape3 = arrayListOf(
    Star(25.0, 6.0, "B"),
    Star(25.0, 5.0, "C"),
    Star(27.0, 5.0, "E"),
    Star(25.0, 3.0, "D")
)
val testShape4 = arrayListOf(
    Star(29.0, 13.0, "B"),
    Star(30.0, 13.0, "C"),
    Star(32.0, 13.0, "E"),
    Star(30.0, 15.0, "D")
)
