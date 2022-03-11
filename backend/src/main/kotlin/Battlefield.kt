class Battlefield {
    companion object {
        private const val SIDE_SIZE = 7
    }

    val matrix = Matrix(SIDE_SIZE)

    fun addShip(point1: Point, point2: Point = point1) {
        val ship = pointsRange(point1, point2)
        ship.forEach {
            matrix[it] = BattlefieldCell.FILLED
        }
        ship.spaceAround()
            .forEach { matrix[it] = BattlefieldCell.EMPTY }
    }

    private fun valid(point: Point) =
        point.x in 0 until SIDE_SIZE &&
                point.y in 0 until SIDE_SIZE

    private fun isVertical(ship: List<Point>) = ship.first().x == ship.last().x

    private fun List<Point>.spaceAround(): List<Point> {
        val first = this.first()
        val last = this.last()

        val upperLeft = Point(first.x - 1, first.y - 1)
        val lowerLeft: Point
        val upperRight: Point
        val lowerRight = Point(last.x + 1, last.y + 1)

        if (isVertical(this)) {
            lowerLeft = Point(last.x - 1, last.y + 1)
            upperRight = Point(first.x + 1, first.y - 1)
        } else {
            lowerLeft = Point(first.x - 1, first.y + 1)
            upperRight = Point(last.x + 1, last.y - 1)
        }

        return pointsRange(upperLeft, upperRight)
            .union(pointsRange(upperLeft, lowerLeft))
            .union(pointsRange(upperRight, lowerRight))
            .union(pointsRange(lowerLeft, lowerRight))
            .filter { valid(it) }
    }
}

fun pointsRange(point1: Point, point2: Point) =
    if (point1.x == point2.x)
        (point1.y..point2.y).map { Point(point1.x, it) }
    else
        (point1.x..point2.x).map { Point(it, point1.y) }
