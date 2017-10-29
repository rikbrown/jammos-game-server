package net.jammos.gameserver.network.message.client

import com.google.common.io.ByteStreams
import mu.KLogging
import net.jammos.gameserver.network.ClientCommand
import net.jammos.gameserver.network.ClientCommand.AUTH_SESSION
import net.jammos.gameserver.network.message.crypto.MessageCrypto
import net.jammos.utils.extensions.readBytes
import net.jammos.utils.field
import java.io.DataInput

interface ClientMessage {
    companion object: KLogging() {
        private val lookup = mapOf(
                AUTH_SESSION to ClientAuthSessionMessage.Companion,
                ClientCommand.PING to ClientPingMessage,
                ClientCommand.CHAR_ENUM to ClientCharEnumMessage)

        fun read(input: DataInput, crypto: MessageCrypto): ClientMessage {
            // Read header
            val header = Header.read(input, crypto)

            // Lookup handler
            val handler = lookup[header.command]
                    ?: throw UnsupportedCommandException(header.command)

            // Delegate
            logger.debug { "Delegating to $handler" }

            val message = handler.readBody(input)
            logger.debug { "Read body $message" }

            return message
        }

    }

    data class Header(val size: Int, val command: ClientCommand) {
        companion object {
            fun read(input: DataInput, crypto: MessageCrypto): Header {
                // Header: 2b + 4b
                // size + command
                val bytes = input.readBytes(6)

                // Decrypt
                val decryptedInput = ByteStreams.newDataInput(crypto.decrypt(bytes))

                // Read size
                val size = field("size") { decryptedInput.readUnsignedShort() }
                logger.debug { "Client message size($size)"}

                // Read command
                val command = field("command") { ClientCommand.read(decryptedInput) }
                logger.debug { "Read clientCommand($command)" }

                return Header(size, command)
            }
        }
    }

    interface Reader<out T: ClientMessage> {
        fun readBody(input: DataInput): T
    }

    class UnsupportedCommandException(cmd: ClientCommand): IllegalArgumentException("Unsupported command: $cmd)")

}