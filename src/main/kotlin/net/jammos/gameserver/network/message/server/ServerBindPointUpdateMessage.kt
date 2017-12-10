package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.Position
import net.jammos.gameserver.network.ServerCommand

data class ServerBindPointUpdateMessage(
        private val position: Position,
        private val mapId: Int,
        private val areaId: Int) : ServerMessage(ServerCommand.BIND_POINT_UPDATE) {
    override val size = 5 * 4

    override fun writeData(output: ByteBuf) {
        with(output) {
            writeFloatLE(position.x)
            writeFloatLE(position.y)
            writeFloatLE(position.z)
            writeIntLE(mapId)
            writeIntLE(areaId)
        }
    }
}
