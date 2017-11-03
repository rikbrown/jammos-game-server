package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import mu.KLogging
import net.jammos.gameserver.network.ServerCommand
import net.jammos.gameserver.network.message.crypto.MessageCrypto

abstract class ServerMessage(val command: ServerCommand) {
    companion object : KLogging()

    abstract fun writeData(output: ByteBuf)
    abstract val size: Int

    fun write(output: ByteBuf, crypto: MessageCrypto) {
        // Payload = 4b + {size}b
        // header + data
        Header(size, command).write(output, crypto)
        writeData(output)
    }

    data class Header(val size: Int, val command: ServerCommand) {

        fun write(output: ByteBuf, crypto: MessageCrypto) {
            // Header payload = 2b + 2b
            // size (uint16) + command (unit16, LE)
            val unencrypted = Unpooled.buffer(4)
                    .writeShort(size + 2)
                    .writeShortLE(command.value.toInt())
                    .array()

            output.writeBytes(crypto.encrypt(unencrypted))
        }

    }

}