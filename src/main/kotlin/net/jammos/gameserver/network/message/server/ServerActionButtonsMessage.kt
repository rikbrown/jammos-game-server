package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.ACTION_BUTTONS

private const val MAX_ACTION_BUTTONS = 120

// TODO: actually define action buttons
object ServerActionButtonsMessage: ServerMessage(ACTION_BUTTONS) {
    override val size = MAX_ACTION_BUTTONS * 4

    override fun writeData(output: ByteBuf) {
        for (i in 1..MAX_ACTION_BUTTONS) {
            output.writeInt(0)
        }
    }
}
