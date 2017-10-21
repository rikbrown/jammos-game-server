package net.jammos.gameserver.network

import mu.KLogging
import net.jammos.utils.extensions.readUnsignedIntLe
import net.jammos.utils.types.WriteableByte
import java.io.DataInput

enum class ClientCommand(override val value: Int): WriteableByte {
    /**
     * Session authentication challenge
     */
    AUTH_SESSION(0x1ED);

    companion object: KLogging() {
        /**
         * Read a little-endian uint32 from the input and convert it to a [ClientCommand]
         */
        fun read(input: DataInput): ClientCommand? {
            val int = input.readUnsignedIntLe()
            return values().find { v -> v.value == int }
        }
    }
}
enum class ServerCommand(override val value: Int): WriteableByte {
    AUTH_CHALLENGE(0x1EC);
}