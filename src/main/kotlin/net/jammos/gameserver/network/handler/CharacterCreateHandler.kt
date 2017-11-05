package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.jammos.gameserver.characters.CharacterListManager
import net.jammos.gameserver.characters.CharacterListManager.CharacterCreationFailure
import net.jammos.gameserver.characters.CharacterListManager.CharacterCreationFailure.CharacterCreationDisabled
import net.jammos.gameserver.characters.CharacterListManager.CharacterCreationFailure.IllegalCharacterName.*
import net.jammos.gameserver.network.message.client.ClientCharCreateMessage
import net.jammos.gameserver.network.message.server.ServerCharCreateResultMessage
import net.jammos.gameserver.network.message.server.ServerCharCreateResultMessage.CharacterCreateStatus.*

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

    override fun handleException(ctx: ChannelHandlerContext, cause: Throwable) {
        // Always respond with an error from this handler rather than kicking them
        respond(ctx, ServerCharCreateResultMessage(when(cause) {
            is CharacterCreationFailure -> when(cause) { // nest to catch all sealed cases
                is CharacterCreationDisabled -> CREATE_DISABLED
                is CharacterNameBlank -> NAME_EMPTY
                is CharacterNameTooShort -> NAME_TOO_SHORT
                is CharacterNameTooLong -> NAME_TOO_LONG
                is CharacterNameReserved -> NAME_RESERVED
                is CharacterNameInUse -> NAME_IN_USE
            }
            else -> FAILED
        }))
    }

    companion object: KLogging()
}