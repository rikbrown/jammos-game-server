package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.ACCOUNT_DATA_TIMES

object ServerAccountDataTimesMessage: ServerMessage(ACCOUNT_DATA_TIMES) {
    override val size = 4 * 32

    override fun writeData(output: ByteBuf) {
        for (i in 1..32) {
            output.writeIntLE(0)
        }
    }
}