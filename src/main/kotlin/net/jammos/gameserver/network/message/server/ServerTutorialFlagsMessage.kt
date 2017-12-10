package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.TUTORIAL_FLAGS
import net.jammos.utils.checkArgument

private const val ACCOUNT_TUTORIAL_COUNT = 8
data class ServerTutorialFlagsMessage(
        private val tutorials: IntArray) : ServerMessage(TUTORIAL_FLAGS) {
    override val size = 4 * ACCOUNT_TUTORIAL_COUNT

    init {
        checkArgument(tutorials.size == ACCOUNT_TUTORIAL_COUNT,
                "Expected $ACCOUNT_TUTORIAL_COUNT tutorials")
    }

    override fun writeData(output: ByteBuf) {
        with(output) {
            tutorials.forEach { writeIntLE(it) }
        }
    }

    companion object {
        val DEFAULT = ServerTutorialFlagsMessage(
                tutorials = (1..ACCOUNT_TUTORIAL_COUNT).map { 0xFFFFFFFF.toInt() }.toIntArray())
    }
}
