package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.CHAR_CREATE_RESULT
import net.jammos.utils.extensions.writeByte
import net.jammos.utils.types.WriteableByte

data class ServerCharCreateResultMessage(
        val status: CharacterCreateStatus): ServerMessage(CHAR_CREATE_RESULT) {

    override val size = 1 // 1 byte status

    override fun writeData(output: ByteBuf) {
        output.writeByte(status)
    }

    enum class CharacterCreateStatus(override val value: Int): WriteableByte {
        SUCCESS(0x2E),
        ERROR(0x2F),
        FAILED(0x30),
        CREATE_DISABLED(0x3A),
        PVP_TEAM_VIOLATION(0x33), // Already has a character on another team
        REALM_LIMIT_REACHED(0x34), // Player's realm limit reached

        NAME_IN_USE(0x31),
        NAME_EMPTY(0X43),
        NAME_TOO_SHORT(0x44),
        NAME_TOO_LONG(0x45),
        NAME_INVALID_CHARACTERS(0x46),
        NAME_MIXED_LANGUAGES(0x47),
    }

}
