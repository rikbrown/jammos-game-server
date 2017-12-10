package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand

object ServerSetRestStartMessage: ServerMessage(ServerCommand.SET_REST_START) {
    override val size = 4

    override fun writeData(output: ByteBuf) {
        output.writeInt(0) // unknown, may be rest state time or experience
    }
}