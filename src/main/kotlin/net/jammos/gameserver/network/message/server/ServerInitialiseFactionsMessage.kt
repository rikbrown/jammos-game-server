package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.INITIALISE_FACTIONS

// TODO: actually send some factions
object ServerInitialiseFactionsMessage : ServerMessage(INITIALISE_FACTIONS) {
    override val size =  4 + (64 * 5)

    override fun writeData(output: ByteBuf) {
        with(output) {
            writeIntLE(0x00000040)
            for (i in 1..64) {
                writeByte(0)
                writeInt(0)
            }
        }
    }
}

