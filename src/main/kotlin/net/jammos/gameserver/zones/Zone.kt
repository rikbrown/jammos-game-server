package net.jammos.gameserver.zones

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING
import com.fasterxml.jackson.annotation.JsonValue

data class Zone @JsonCreator(mode=DELEGATING) constructor(@JsonValue val zone: Int) {
    companion object {
        val NONE = Zone(0)
        val DUN_MOROGH = Zone(1)
        val LONGSHORE = Zone(2)
    }
}