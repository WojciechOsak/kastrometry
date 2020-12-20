package wojciechosak.kastrometry.model

data class Star(val x: Double, val y: Double, val name: String = "($x,$y)") {
    constructor(x: Int, y: Int, name: String = "($x,$y)") : this(x.toDouble(), y.toDouble(), name)
}