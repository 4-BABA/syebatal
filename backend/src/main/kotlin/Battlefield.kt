class Battlefield {
    companion object {
        private const val SIDE_SIZE = 7
    }

    val matrix = Matrix(SIDE_SIZE)

    fun addShip(point1: Point) {
        addShip(point1, point1)
    }

    fun addShip(point1: Point, point2: Point) {
        val shipCoords = pointsRange(point1, point2)
        shipCoords.forEach {
            matrix.set(it, BattlefieldValue.FILLED)
        }
        val spaceAround = shipCoords.spaceAround()
        spaceAround.forEach { matrix.set(it, BattlefieldValue.EMPTY) }
    }

    private fun valid(point: Point) =
        point.x in 0 until SIDE_SIZE &&
                point.y in 0 until SIDE_SIZE

    private fun List<Point>.spaceAround(): List<Point> {
        val vertical = this.first().x == this.last().x
        val spaceAround = mutableListOf<Point>()
        for (point in this) {
            val newCoords =
                if (vertical)
                    listOf(
                        Point(point.x - 1, point.y),
                        Point(point.x + 1, point.y))
                else
                    listOf(
                        Point(point.x, point.y - 1),
                        Point(point.x, point.y + 1))
            newCoords.forEach { newCoord ->
                if (valid(newCoord)) {
                    spaceAround.add(newCoord)
                }
            }
        }
        // visualisation for vertical ship
        // [ prePreStart ]    ....    [ postPreStart ]
        //      ....        [ ship ]        ....
        //  [ prePostEnd ]    ....    [ postPostEnd ]
        val twoRanges =
            if (vertical) {
                val prePreStart = Point(this.first().x - 1, this.first().y - 1)
                val postPreStart = Point(this.first().x + 1, this.first().y - 1)
                val prePostEnd = Point(this.last().x - 1, this.last().y + 1)
                val postPostEnd = Point(this.last().x + 1, this.last().y + 1)
                listOf(
                    pointsRange(prePreStart, postPreStart),
                    pointsRange(prePostEnd, postPostEnd)
                )
            } else {
                val prePreStart = Point(this.first().x - 1, this.first().y - 1)
                val postPreStart = Point(this.first().x - 1, this.first().y + 1)
                val prePostEnd = Point(this.last().x + 1, this.last().y - 1)
                val postPostEnd = Point(this.last().x + 1, this.last().y + 1)
                listOf(
                    pointsRange(prePreStart, postPreStart),
                    pointsRange(prePostEnd, postPostEnd)
                )
            }
        twoRanges.map { range ->
            range.forEach { point -> if (valid(point)) spaceAround.add(point) }
        }
        return spaceAround
    }
}

fun main() {
    val battlefield = Battlefield()
    battlefield.addShip(Point(0, 0), Point(2, 0))
    battlefield.addShip(Point(4, 2), Point(4, 3))
    battlefield.addShip(Point(1, 5))
    battlefield.addShip(Point(6, 6))
    battlefield.matrix.prettyPrint()
}

fun pointsRange(points1: Point, points2: Point) =
    if (points1.x == points2.x)
        (points1.y..points2.y).map { Point(points1.x, it) }
    else
        (points1.x..points2.x).map { Point(it, points1.y) }
