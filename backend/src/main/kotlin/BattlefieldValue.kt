import java.lang.IllegalArgumentException

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
