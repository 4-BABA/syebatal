package io.baba4.syebatal.bf

import io.baba4.syebatal.Battlefield
import io.baba4.syebatal.BfCell


open class BattlefieldTest {
    protected val customBattlefield = Battlefield(size = 3) { (row, column) ->
        when (row to column) {
            1 to 0, 1 to 2 -> BfCell.UNCHECKED
            0 to 0, 1 to 1 -> BfCell.EMPTY
            0 to 2, 2 to 0, 2 to 1 -> BfCell.FILLED
            0 to 1, 2 to 2 -> BfCell.DAMAGED
            else -> throw IllegalArgumentException("Size should be 3.")
        }
    }
    protected val customSerialization = "3|•☒☐ • ☐☐☒"
}
