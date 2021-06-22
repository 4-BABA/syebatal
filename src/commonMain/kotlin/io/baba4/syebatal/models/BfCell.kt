package io.baba4.syebatal.models


enum class BfCell(val symbol: Char, val isShip: Boolean) {
    UNCHECKED(symbol = 'o', isShip = false),
    EMPTY(symbol = '•', isShip = false),
    FILLED(symbol = 'x', isShip = true),
    DAMAGED(symbol = '☒', isShip = true);

    val isNotShip: Boolean get() = !isShip


    companion object {
        fun fromSymbol(symbol: Char) = values().find { it.symbol == symbol } ?: illegalSymbol(symbol)

        private fun illegalSymbol(symbol: Char): Nothing = throw IllegalArgumentException(
            "Can't find \'${BfCell::class.simpleName}\' for $symbol. Only ${values().map { it.symbol }} are allowed."
        )
    }
}
