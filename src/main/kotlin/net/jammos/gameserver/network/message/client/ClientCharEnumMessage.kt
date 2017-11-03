package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf

object ClientCharEnumMessage: ClientMessage, ClientMessage.Reader<ClientCharEnumMessage> {
    override fun readBody(input: ByteBuf) = ClientCharEnumMessage
}