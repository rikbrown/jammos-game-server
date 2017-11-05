package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf

object ClientUpdateAccountDataMessage: ClientMessage, ClientMessage.Reader<ClientUpdateAccountDataMessage> {
    override fun readBody(input: ByteBuf): ClientUpdateAccountDataMessage {
        return ClientUpdateAccountDataMessage
    }
}