package net.jammos.gameserver.network.message.client

import com.google.common.collect.Maps.immutableEnumMap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import mu.KLogging
import net.jammos.gameserver.network.ClientCommand
import net.jammos.gameserver.network.ClientCommand.AUTH_SESSION
import net.jammos.gameserver.network.message.crypto.MessageCrypto
import net.jammos.utils.extensions.readByteArray
import net.jammos.utils.field

interface ClientMessage {
    companion object: KLogging() {
        // TODO: use reflection and annotations to discover these?
        private val lookup = immutableEnumMap(mapOf(
                AUTH_SESSION to ClientAuthSessionMessage.Companion,
                ClientCommand.PING to ClientPingMessage,
                ClientCommand.CHAR_ENUM to ClientCharEnumMessage,
                ClientCommand.CHAR_CREATE to ClientCharCreateMessage.Companion,
                ClientCommand.CHAR_DELETE to ClientCharDeleteMessage.Companion))

        fun read(input: ByteBuf, crypto: MessageCrypto): ClientMessage {
            // Read header
            val header = Header.read(input, crypto)

            // Lookup handler
            val handler = lookup[header.command] ?: throw UnsupportedCommandException(header.command)

            // Delegate
            logger.debug { "Delegating to $handler" }

            val message = handler.readBody(input)
            logger.debug { "Read body $message" }

            return message
        }

    }

    data class Header(val size: Int, val command: ClientCommand) {
        companion object {
            fun read(input: ByteBuf, crypto: MessageCrypto): Header {
                // Header: 2b + 4b
                // size + command
                val bytes = input.readByteArray(6)

                // Decrypt
                val decryptedInput = Unpooled.wrappedBuffer(crypto.decrypt(bytes))

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
        fun readBody(input: ByteBuf): T
    }

    class UnsupportedCommandException(cmd: ClientCommand): IllegalArgumentException("Unsupported command: $cmd)")

}