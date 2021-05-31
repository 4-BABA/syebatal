import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import kotlin.math.round
import kotlin.math.sqrt

class Battlefield {
    companion object {
        const val SIDE_SIZE = 10
    }

    private val matrix = Matrix(SIDE_SIZE)

}

enum class BattlefieldValue {
    UNKNOWN, EMPTY, FILLED;

    override fun toString() = when (this) {
        UNKNOWN -> " "
        EMPTY -> "·"
        FILLED -> "x"
    }

    companion object {
        fun toVal(c: Char) = when (c) {
             ' ' -> UNKNOWN
             '·', '.' -> EMPTY
             'x', 'X' -> FILLED
            else -> throw IllegalArgumentException()
        }
    }
}

// : Iterable<BattlefieldValue>
class Matrix {

    private val size: Int
    private val matrix: Array<Array<BattlefieldValue>>

    constructor(size: Int) {
        this.size = size
        this.matrix = Array(size) { Array(size) { BattlefieldValue.UNKNOWN } }
    }

    constructor(chars: CharArray) {
        val rawRoot = sqrt(chars.size.toDouble())
        val root = round(rawRoot)
        if (rawRoot != root) {
            throw IllegalArgumentException()
        }
        size = root.toInt()
        this.matrix = Array(size) { Array(size) { BattlefieldValue.UNKNOWN } }
        val chunkedChars = chars.toList().chunked(size)
        chunkedChars.withIndex().forEach { (rowIdx, chunk) ->
            val row = getRow(rowIdx)
            chunk.withIndex().forEach { (colIdx, c) ->
                row[colIdx] = BattlefieldValue.toVal(c)
            }
        }
    }

    constructor(str: String) : this(str.toCharArray())

    fun get(rowNum: Int, colNum: Int): BattlefieldValue {
//        if (rowNum < 0 || size <= rowNum) {

//        }
        return matrix[rowNum][colNum]
    }
    fun set(rowNum: Int, colNum: Int, value: BattlefieldValue) {
        matrix[rowNum][colNum] = value
    }

    fun getRow(num: Int) = matrix[num]
    fun getCol(num: Int) = matrix.map { arr -> arr[num] }.toTypedArray()

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        matrix.forEach { row -> row.forEach {
            cell -> stringBuilder.append(cell.toString())
        } }
        return stringBuilder.toString()
    }

    fun prettyPrint() {
        matrix.forEach { row ->
            row.withIndex().forEach { (index, cell) ->
                print(if (index == row.size - 1) "$cell " else "$cell | ")
            }
            println()
        }
    }
}

fun main() {
    println(Matrix(charArrayOf(
            ' ', ' ', ' ', ' ',
            ' ', '.', '.', '.',
            ' ', '.', 'x', '.',
            ' ', '.', 'x', '.')))
    Matrix(("     .x" +
            " ... .." +
            " .x.   " +
            " .x.   " +
            " ......" +
            "  .xxx." +
            "  .....")).prettyPrint()
}
