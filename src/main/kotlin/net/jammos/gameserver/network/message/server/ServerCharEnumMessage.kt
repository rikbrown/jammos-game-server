package net.jammos.gameserver.network.message.server

import net.jammos.gameserver.network.ServerCommand.CHAR_ENUM
import java.io.DataOutput

data class ServerCharEnumMessage(val foo: Int): ServerMessage(CHAR_ENUM) {
    // structure: 1b [char count (UInt8)]
    override val size = 1

    override fun writeData(output: DataOutput) {
        output.writeByte(0) // character count
    }

}
