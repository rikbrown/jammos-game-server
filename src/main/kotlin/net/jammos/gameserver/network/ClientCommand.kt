package net.jammos.gameserver.network

import mu.KLogging
import net.jammos.utils.extensions.readUnsignedIntLe
import net.jammos.utils.extensions.toHexString
import net.jammos.utils.types.WriteableByte
import java.io.DataInput

enum class ClientCommand(override val value: Int): WriteableByte {
    /**
     * Session authentication challenge
     */
    AUTH_SESSION(0x1ED),
    CHAR_ENUM(0x037),
    PING(0x1DC);

    companion object: KLogging() {
        /**
         * Read a little-endian uint32 from the input and convert it to a [ClientCommand]
         * @throws IllegalCommandException if the command was not recognised
         */
        fun read(input: DataInput): ClientCommand {
            val int = input.readUnsignedIntLe()
            return values().find { v -> v.value == int }
                ?: throw IllegalCommandException(int)
        }
    }

    override fun toString() = "${super.toString()} (${value.toHexString(3)})"

    class IllegalCommandException(cmd: Int): IllegalArgumentException("Illegal command: $cmd (${cmd.toHexString(3)})")
}

enum class ServerCommand(override val value: Int): WriteableByte {
    AUTH_CHALLENGE(0x1EC),
    AUTH_RESPONSE(0x1EE),
    PONG(0x1DD),
    CHAR_ENUM(0x03B);

    override fun toString() = "${super.toString()} (${value.toHexString(3)})"
}

enum class ResponseCode(override val value: Int): WriteableByte {
    AUTH_OK(0x0C),
    AUTH_UNKNOWN_ACCOUNT(0x15),
    AUTH_BANNED(0x1C),
    AUTH_FAILED(0x0D)
}

