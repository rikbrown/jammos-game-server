package net.jammos.gameserver.network.message.client

import mu.KLogging
import net.jammos.gameserver.network.ClientCommand
import net.jammos.utils.field
import java.io.DataInput

interface ClientMessage {
    companion object: KLogging() {
        private val lookup = mapOf(
                ClientCommand.AUTH_SESSION to ClientAuthSessionMessage.Companion)

        fun read(input: DataInput): ClientMessage {
            // Read size
            val size = field("size") { input.readUnsignedShort() }
            logger.debug { "Client message size($size)"}

            // Read command
            val command = field("command") { ClientCommand.read(input) }
            logger.debug { "Read serverCommand($command)" }

            // Lookup handler
            val handler = lookup[command] ?: throw IllegalArgumentException("Unsupported command")

            // Delegate
            logger.debug { "Delegating to $handler" }

            val message = handler.readBody(input)
            logger.debug { "Read body $message" }

            return message
        }

    }

    interface Reader<out T: ClientMessage> {
        fun readBody(input: DataInput): T
    }

}