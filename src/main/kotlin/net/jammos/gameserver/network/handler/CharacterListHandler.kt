package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import net.jammos.gameserver.characters.CharacterListManager
import net.jammos.gameserver.network.message.client.ClientCharEnumMessage
import net.jammos.gameserver.network.message.server.ServerCharEnumMessage

class CharacterListHandler(private val characterListManager: CharacterListManager): AuthenticatedJammosHandler() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientCharEnumMessage) return pass(ctx, msg)

        // Reply with empty character list
        respond(ctx, ServerCharEnumMessage(characters = characterListManager.getCharacters(username(ctx))))
    }

}