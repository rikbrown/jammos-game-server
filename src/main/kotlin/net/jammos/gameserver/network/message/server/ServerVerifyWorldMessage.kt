package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.LOGIN_VERIFY_WORLD

data class ServerVerifyWorldMessage(
        val mapId: Int,
        val positionX: Float,
        val positionY: Float,
        val positionZ: Float,
        val orientation: Float): ServerMessage(LOGIN_VERIFY_WORLD) {

    override val size = 20

    override fun writeData(output: ByteBuf) {
        with (output) {
            writeIntLE(mapId)
            writeFloatLE(positionX)
            writeFloatLE(positionY)
            writeFloatLE(positionZ)
            writeFloatLE(orientation)
        }
    }
}