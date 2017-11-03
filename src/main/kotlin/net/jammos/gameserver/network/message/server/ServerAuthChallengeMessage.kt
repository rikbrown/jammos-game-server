package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.AUTH_CHALLENGE
import net.jammos.utils.checkArgument

data class ServerAuthChallengeMessage(private val seed: ByteArray): ServerMessage(AUTH_CHALLENGE) {
    override val size = seed.size

    init {
        checkArgument(size == 4, { "Expected 4 byte seed" })
    }

    override fun writeData(output: ByteBuf) {
        output.writeBytes(seed)
    }
}