package io.baba4.syebatal

import kotlin.math.sqrt


class Battlefield(val size: Int, initial: (row: Int, column: Int) -> BfCell) {
    init {
        require(size >= 0) { "Can't create ${Battlefield::class.simpleName} with negative size [$size]." }
    }
    val table: Array<Array<BfCell>> = Array(size) { row -> Array(size) { column -> initial(row, column) } }

    override fun equals(other: Any?): Boolean =
        other is Battlefield && this.size == other.size && this.table.contentDeepEquals(other.table)

    override fun hashCode(): Int =
        this.size.hashCode() + 31 * table.contentDeepHashCode()


    fun encode() = this.table.joinToString(separator = "", prefix = "${this.size}$SEPARATOR") { row ->
        row.joinToString(separator = "") { it.symbol.toString() }
    }

    companion object {
        private const val SEPARATOR = '|'

        fun decode(string: String): Battlefield {
            require(string.length >= 0) {
                "Can't decode ${Battlefield::class.simpleName} with negative length [${string.length}]."
            }

            val (sizeString, content) = string.split(SEPARATOR, limit = 2)
            val size = sizeString.toInt()
            val sizeByContent = sqrt(content.length.toDouble()).toInt()
            require(sizeByContent == size) {
                "Invalid decoded matrix. Size should be $size, but actual $sizeByContent."
            }

            return Battlefield(size) { row, column -> BfCell.fromSymbol(content[row * size + column]) }
        }
    }
}
