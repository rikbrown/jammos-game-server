package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import net.jammos.gameserver.characters.CharacterListManager
import net.jammos.gameserver.network.message.client.ClientCharDeleteMessage
import net.jammos.gameserver.network.message.server.ServerCharDeleteResultMessage
import net.jammos.gameserver.network.message.server.ServerCharDeleteResultMessage.CharacterDeleteStatus.SUCCESS

/**
 * Deletes a character
 *
 * @notes see Elysium void WorldSession::HandleCharDeleteOpcode(WorldPacket & recv_data)
 */
class CharacterDeleteHandler(private val characterListManager: CharacterListManager): AuthenticatedGameHandler() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientCharDeleteMessage) return pass(ctx, msg)

        // Delete character
        characterListManager.deleteCharacter(userId(ctx), msg.characterId)

        // Success
        respond(ctx, ServerCharDeleteResultMessage(SUCCESS))
    }

}