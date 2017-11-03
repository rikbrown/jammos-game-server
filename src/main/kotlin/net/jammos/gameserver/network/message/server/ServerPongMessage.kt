package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.PONG

data class ServerPongMessage(val ping: Long): ServerMessage(PONG) {
    // structure: 4b [ping (UInt32)]
    override val size = 4

    override fun writeData(output: ByteBuf) {
        output.writeIntLE(ping.toInt())
    }

}