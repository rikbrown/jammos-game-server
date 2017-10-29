package net.jammos.gameserver.network.message.server

import mu.KLogging
import net.jammos.gameserver.network.ServerCommand
import net.jammos.gameserver.network.message.crypto.MessageCrypto
import java.io.DataOutput
import java.nio.ByteBuffer
import java.nio.ByteOrder

abstract class ServerMessage(val command: ServerCommand) {
    companion object : KLogging()

    abstract fun writeData(output: DataOutput)
    abstract val size: Int

    fun write(output: DataOutput, crypto: MessageCrypto) {
        // Payload = 4b + {size}b
        // header + data
        Header(size, command).write(output, crypto)
        writeData(output)
    }

    data class Header(val size: Int, val command: ServerCommand) {

        fun write(output: DataOutput, crypto: MessageCrypto) {
            // Header payload = 2b + 2b
            // size (uint16) + command (unit16, LE)
            val unencrypted = ByteBuffer
                    .allocate(4)
                    .putShort((size + 2).toShort())
                    .order(ByteOrder.LITTLE_ENDIAN) // this works but changing order midway might be sketch.
                    .putShort(command.value.toShort())
                    .array()

            output.write(crypto.encrypt(unencrypted))
        }

    }

}