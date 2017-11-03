package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf
import net.jammos.utils.field

data class ClientPingMessage(
        val ping: Long,
        val latency: Long
): ClientMessage {

    companion object: ClientMessage.Reader<ClientPingMessage> {
        override fun readBody(input: ByteBuf): ClientPingMessage {
            return with (input) {
                // @formatter:off
                ClientPingMessage(
                    ping = field("ping")       { readUnsignedIntLE() },
                    latency = field("latency") { readUnsignedIntLE() })
                // @formatter:on
            }
        }

    }

}