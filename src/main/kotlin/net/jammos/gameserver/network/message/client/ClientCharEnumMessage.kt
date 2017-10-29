package net.jammos.gameserver.network.message.client

import java.io.DataInput

object ClientCharEnumMessage: ClientMessage, ClientMessage.Reader<ClientCharEnumMessage> {
    override fun readBody(input: DataInput) = ClientCharEnumMessage
}