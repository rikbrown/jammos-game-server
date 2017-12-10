package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.INITIAL_SPELLS

// TODO: actually send some spells
object ServerInitialSpellsMessage : ServerMessage(INITIAL_SPELLS) {
    override val size = 1 + 2 + 0 + 2 + 0

    override fun writeData(output: ByteBuf) {
        with(output) {
            writeByte(0)
            writeShort(0) // 0 spells
            writeShort(0) // 0 spell cooldowns
        }
    }
}
