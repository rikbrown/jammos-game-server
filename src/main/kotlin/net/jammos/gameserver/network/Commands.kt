package net.jammos.gameserver.network

import io.netty.buffer.ByteBuf
import net.jammos.utils.extensions.toHexString
import net.jammos.utils.types.ReversibleByte
import net.jammos.utils.types.WriteableByte

enum class ClientCommand(override val value: Short): WriteableByte {
    /**
     * Session authentication challenge
     */
    PING(0x1DC),
    AUTH_SESSION(0x1ED),
    CHAR_CREATE(0x036),
    CHAR_ENUM(0x037),
    CHAR_DELETE(0x038),
    LOGIN(0x03D),
    UPDATE_ACCOUNT_DATA(0x20B);

    companion object: ReversibleByte<ClientCommand>(values()) {
        /**
         * Read a little-endian uint32 from the input and convert it to a [ClientCommand]
         * @throws IllegalCommandException if the command was not recognised
         */
        fun read(input: ByteBuf): ClientCommand {
            val int = input.readUnsignedIntLE()
            return ofValueOrNull(int.toShort()) ?: throw IllegalCommandException(int)
        }
    }

    override fun toString() = "${super.toString()} (${value.toHexString(3)})"

    class IllegalCommandException(cmd: Long): IllegalArgumentException("Illegal command: $cmd (${cmd.toHexString(3)})")
}

enum class ServerCommand(override val value: Short): WriteableByte {
    AUTH_CHALLENGE(0x1EC),
    AUTH_RESPONSE(0x1EE),
    PONG(0x1DD),
    CHAR_CREATE_RESULT(0x03A),
    CHAR_ENUM(0x03B),
    CHAR_DELETE_RESULT(0x03C),
    LOGIN_VERIFY_WORLD(0x236),
    ACCOUNT_DATA_TIMES(0x209);

    override fun toString() = "${super.toString()} (${value.toHexString(3)})"
}


