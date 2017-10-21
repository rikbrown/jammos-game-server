package net.jammos.gameserver.network.message.server

import net.jammos.gameserver.network.ServerCommand.AUTH_CHALLENGE
import net.jammos.utils.checkArgument
import net.jammos.utils.extensions.write
import java.io.DataOutput

data class ServerAuthChallengeMessage(val seed: ByteArray): ServerMessage(AUTH_CHALLENGE) {
    override val size = seed.size

    init {
        checkArgument(size == 4, { "Expected 4 byte seed" })
    }

    override fun writeData(output: DataOutput) {
        seed.write(output)
    }
}