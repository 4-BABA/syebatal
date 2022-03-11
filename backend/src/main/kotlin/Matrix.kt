import kotlin.math.round
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int)

typealias MatrixArray<T> = Array<Array<T>>

inline fun <reified T> matrixArray(size: Int, initial: T): MatrixArray<T> {
    require(size > 0)
    return Array(size) { Array(size) { initial } }
}

class Matrix : Iterable<BattlefieldCell> {

    private val size: Int
    private val matrix: MatrixArray<BattlefieldCell>

    constructor(size: Int) {
        this.size = size
        this.matrix = matrixArray(size, BattlefieldCell.UNKNOWN)
    }

    constructor(chars: CharArray) {
        val rawRoot = sqrt(chars.size.toDouble())
        val root = round(rawRoot)
        if (rawRoot != root) {
            throw IllegalArgumentException()
        }
        size = root.toInt()
        matrix = matrixArray(size, BattlefieldCell.UNKNOWN)
        val chunkedChars = chars.toList().chunked(size)
        chunkedChars.forEachIndexed { row, chunk ->
            chunk.forEachIndexed { col, char ->
                matrix[row][col] = BattlefieldCell.fromSymbol(char)
            }
        }
    }

    constructor(str: String) : this(str.toCharArray())

    fun get(row: Int, col: Int) = matrix[row][col]
    fun set(row: Int, col: Int, value: BattlefieldCell) {
        matrix[row][col] = value
    }

    operator fun set(points: Point, value: BattlefieldCell) {
        matrix[points.y][points.x] = value
    }

    fun getRow(num: Int) = matrix[num]
    fun getCol(num: Int) = matrix.map { arr -> arr[num] }.toTypedArray()

    override fun toString() =
        matrix.joinToString(separator = "") { it.joinToString(separator = "") }

    fun prettyPrint() {
        matrix.forEach { row ->
            row.forEachIndexed { index, cell ->
                print(if (index == row.size - 1) "$cell " else "$cell | ")
            }
            println()
        }
    }

    override fun iterator() = object : Iterator<BattlefieldCell> {
        var row = 0
        var col = 0

        override fun hasNext() = row + 1 < size || col + 1 < size

        override fun next(): BattlefieldCell {
            if (col + 1 < size) {
                col++
            } else {
                row++
                col = 0
            }
            return matrix[row][col]
        }
    }
}
