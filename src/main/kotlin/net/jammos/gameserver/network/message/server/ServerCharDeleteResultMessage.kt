package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.CHAR_DELETE_RESULT
import net.jammos.utils.extensions.writeByte
import net.jammos.utils.types.WriteableByte

data class ServerCharDeleteResultMessage(
        val status: CharacterDeleteStatus): ServerMessage(CHAR_DELETE_RESULT) {

    override val size = 1 // 1 byte status

    override fun writeData(output: ByteBuf) {
        output.writeByte(status)
    }

    enum class CharacterDeleteStatus(override val value: Short): WriteableByte {
        SUCCESS(0x39),
        FAILED(0x3A);
    }

}

