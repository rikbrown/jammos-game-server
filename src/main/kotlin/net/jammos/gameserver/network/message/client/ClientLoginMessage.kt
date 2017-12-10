package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.characters.CharacterId
import net.jammos.gameserver.network.message.client.ClientMessage.Reader
import net.jammos.utils.field

data class ClientLoginMessage(val characterId: CharacterId): ClientMessage {
    // 14b = 6b header + 8b data
    companion object: Reader<ClientLoginMessage> {
        override fun readBody(input: ByteBuf) = ClientLoginMessage(
                characterId = field("characterId") { CharacterId(input.readLongLE()) })
    }
}