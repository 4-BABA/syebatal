package io.baba4.syebatal.elm

import com.baba4.syebatal.models.Player
import com.baba4.syebatal.models.PlayerId

// TODO: implement
interface PlayerStorage {
    fun getPlayer(playerId: PlayerId): Player
}
