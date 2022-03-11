import java.lang.IllegalArgumentException

enum class BattlefieldCell(val char: Char) {
    UNKNOWN(' '), EMPTY('.'), FILLED('x');

    override fun toString(): String {
        return char.toString()
    }

    companion object {
        fun fromSymbol(symbol: Char) =
            values().find { it.char == symbol } ?: throw IllegalArgumentException(
                "No BattleFieldValue for $symbol. Only ${
                    values().map { it.char }
                } allowed.")
    }
}
