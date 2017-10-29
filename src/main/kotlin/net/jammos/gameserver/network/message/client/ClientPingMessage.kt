package net.jammos.gameserver.network.message.client

import net.jammos.utils.extensions.readUnsignedIntLe
import net.jammos.utils.field
import java.io.DataInput

data class ClientPingMessage(
        val ping: Int,
        val latency: Int
): ClientMessage {

    companion object: ClientMessage.Reader<ClientPingMessage> {
        override fun readBody(input: DataInput): ClientPingMessage {
            return with (input) {
                // @formatter:off
                ClientPingMessage(
                    ping = field("ping")       { readUnsignedIntLe() },
                    latency = field("latency") { readUnsignedIntLe() })
                // @formatter:on
            }
        }

    }

}