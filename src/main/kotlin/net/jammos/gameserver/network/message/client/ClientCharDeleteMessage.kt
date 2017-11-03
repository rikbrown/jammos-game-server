package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.characters.CharacterId
import net.jammos.gameserver.network.message.client.ClientMessage.Reader
import net.jammos.utils.field

data class ClientCharDeleteMessage(val characterId: CharacterId): ClientMessage {
    companion object: Reader<ClientCharDeleteMessage> {
        override fun readBody(input: ByteBuf) = ClientCharDeleteMessage(
                characterId = field("characterId") { CharacterId(input.readLongLE()) })
    }
}