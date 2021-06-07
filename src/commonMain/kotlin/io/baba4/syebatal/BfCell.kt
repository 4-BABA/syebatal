package io.baba4.syebatal


enum class BfCell(val symbol: Char) {
    UNCHECKED(symbol = ' '),
    EMPTY(symbol = '•'),
    FILLED(symbol = '☐'),
    DAMAGED(symbol = '☒');


    companion object {
        fun fromSymbol(symbol: Char) = values().find { it.symbol == symbol } ?: illegalSymbol(symbol)

        private fun illegalSymbol(symbol: Char): Nothing = throw IllegalArgumentException(
            "Can't find ${BfCell::class.simpleName} for $symbol. Only ${values().map { it.symbol }} are allowed."
        )
    }
}
