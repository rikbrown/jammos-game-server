package net.jammos.gameserver.network.message.server

import net.jammos.gameserver.network.ServerCommand.PONG
import java.io.DataOutput

data class ServerPongMessage(val ping: Int): ServerMessage(PONG) {
    // structure: 4b [ping (UInt32)]
    override val size = 4

    override fun writeData(output: DataOutput) {
        output.writeInt(ping)
    }

}