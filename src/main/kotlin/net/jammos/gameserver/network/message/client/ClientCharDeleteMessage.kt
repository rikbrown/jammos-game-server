package net.jammos.gameserver.network.message.client

import net.jammos.gameserver.characters.CharacterId
import net.jammos.gameserver.network.message.client.ClientMessage.Reader
import net.jammos.utils.extensions.readLongLe
import net.jammos.utils.field
import java.io.DataInput

data class ClientCharDeleteMessage(val characterId: CharacterId): ClientMessage {
    companion object: Reader<ClientCharDeleteMessage> {
        override fun readBody(input: DataInput) = ClientCharDeleteMessage(
                characterId = field("characterId") { CharacterId(input.readLongLe()) })
    }
}