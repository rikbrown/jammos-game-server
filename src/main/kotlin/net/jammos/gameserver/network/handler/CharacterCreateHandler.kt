package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.jammos.gameserver.characters.CharacterListManager
import net.jammos.gameserver.network.message.client.ClientCharCreateMessage
import net.jammos.gameserver.network.message.server.ServerCharCreateResultMessage
import net.jammos.gameserver.network.message.server.ServerCharCreateResultMessage.CharacterCreateStatus.SUCCESS

/**
 * Handler to create new characters.
 *
 * @notes See Elysium: void WorldSession::HandleCharCreateOpcode(WorldPacket & recv_data)
 */
class CharacterCreateHandler(private val characterListManager: CharacterListManager): AuthenticatedGameHandler() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientCharCreateMessage) return pass(ctx, msg)

        // Create character
        val character = characterListManager.createCharacter(userId(ctx),
                name = msg.name,
                race = msg.race,
                characterClass = msg.characterClass,
                gender = msg.gender,
                skin = msg.skin,
                face = msg.face,
                hairStyle = msg.hairStyle,
                hairColour = msg.hairColour,
                facialHair = msg.facialHair)
        logger.info { "Successfully created character $character" }

        // Respond
        respond(ctx, ServerCharCreateResultMessage(SUCCESS))
    }

    companion object: KLogging()
}