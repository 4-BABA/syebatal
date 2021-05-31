class Battlefield {
    companion object {
        private const val SIDE_SIZE = 7
    }

    val matrix = Matrix(SIDE_SIZE)

    fun addShip(point1: Point) {
        addShip(point1, point1)
    }

    fun addShip(point1: Point, point2: Point) {
        val ship = pointsRange(point1, point2)
        ship.forEach {
            matrix.set(it, BattlefieldValue.FILLED)
        }
        ship.spaceAround()
            .forEach { matrix.set(it, BattlefieldValue.EMPTY) }
    }

    private fun valid(point: Point) =
        point.x in 0 until SIDE_SIZE &&
                point.y in 0 until SIDE_SIZE

    private fun isVertical(ship: List<Point>) = ship.first().x == ship.last().x

    // visualisation for vertical ship
    // [ prePreHead ]    ....    [ postPreHead ]
    //      ....       [ ship ]       ....
    //      ....       [ ship ]       ....
    // [ prePostEnd ]    ....    [ postPostEnd ]
    private fun getPreHeadAndPostTail(ship: List<Point>): Collection<Point> {
        val prePreHead: Point
        val postPreHead: Point
        val prePostEnd: Point
        val postPostEnd: Point
        if (isVertical(ship)) {
            prePreHead = Point(ship.first().x - 1, ship.first().y - 1)
            postPreHead = Point(ship.first().x + 1, ship.first().y - 1)
            prePostEnd = Point(ship.last().x - 1, ship.last().y + 1)
            postPostEnd = Point(ship.last().x + 1, ship.last().y + 1)

        } else {
            prePreHead = Point(ship.first().x - 1, ship.first().y - 1)
            postPreHead = Point(ship.first().x - 1, ship.first().y + 1)
            prePostEnd = Point(ship.last().x + 1, ship.last().y - 1)
            postPostEnd = Point(ship.last().x + 1, ship.last().y + 1)
        }
        val pointsRange = pointsRange(prePreHead, postPreHead)
        return pointsRange.union(pointsRange(prePostEnd, postPostEnd))
    }

    private fun getBodyFat(ship: List<Point>): List<Point> {
        val vertical = isVertical(ship)
        val bodyFat = mutableListOf<Point>()
        ship.forEach { point ->
            if (vertical) {
                bodyFat.add(Point(point.x - 1, point.y))
                bodyFat.add(Point(point.x + 1, point.y))
            } else {
                bodyFat.add(Point(point.x, point.y - 1))
                bodyFat.add(Point(point.x, point.y + 1))
            }
        }
        return bodyFat
    }

    private fun List<Point>.spaceAround(): List<Point> {
        return getBodyFat(this)
            .union(getPreHeadAndPostTail(this))
            .filter { valid(it) }
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
