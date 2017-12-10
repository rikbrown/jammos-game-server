package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.INIT_WORLD_STATES

data class ServerInitWorldStatesMessage(
        val mapId: Int,
        val zoneId: Int) : ServerMessage(INIT_WORLD_STATES) {
    override val size = 4 + 4 + 2 + 6

    override fun writeData(output: ByteBuf) {
        with(output) {
            writeIntLE(mapId)
            writeIntLE(zoneId)
            writeShort(0) // count of uint32 blocks, placeholder
        }
    }
}

