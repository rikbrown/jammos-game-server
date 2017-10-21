package net.jammos.gameserver.network.message.server

import net.jammos.gameserver.network.ServerCommand
import java.io.DataOutput

abstract class ServerMessage(val command: ServerCommand) {
    abstract fun writeData(output: DataOutput)
    abstract val size: Int

    fun write(output: DataOutput) {
        output.writeShort(size + 2)
        command.write16(output)
        writeData(output)
    }


}