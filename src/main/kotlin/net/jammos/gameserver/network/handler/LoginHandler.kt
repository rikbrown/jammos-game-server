package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.jammos.gameserver.Position
import net.jammos.gameserver.network.message.client.ClientLoginMessage
import net.jammos.gameserver.network.message.server.*
import net.jammos.gameserver.zones.Zone

class LoginHandler : AuthenticatedGameHandler() {
    // CharacterHandler // WorldSession::HandlePlayerLogin
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientLoginMessage) return pass(ctx, msg)

        // Send verify world message
        write(ctx, ServerVerifyWorldMessage(0, 0F, 0F, 0F, 0F))

        // Send account data times
        write(ctx, ServerAccountDataTimesMessage)

        // TODO: MOTD
        // TODO: Guild

        // TODO: not slive -> sendcorpsereclaimdelay

        // sendInitialPacketsBeforeAddToMap
        sendInitialPacketsBeforeAddToMap(ctx)

        // bug? sends action buttons again
        write(ctx, ServerActionButtonsMessage)

        // if (!pCurrChar->getCinematic()) { do cinematic }
        // pCurrChar->GetSocial()->SendFriendList();
        // pCurrChar->GetSocial()->SendIgnoreList();

        // sendInitialPacketsAfterAddToMap
        sendInitialPacketsAfterAddToMap(ctx)

        ctx.flush()
    }

    private fun sendInitialPacketsBeforeAddToMap(ctx: ChannelHandlerContext) {
        // unknown, may be rest state time or experience
        write(ctx, ServerSetRestStartMessage)

        // homebind
        write(ctx, ServerBindPointUpdateMessage(
                position = Position.ZERO,
                mapId =  0,
                areaId = Zone.DUN_MOROGH.zone // FIXME???
        ))

        // SendTutorialsData
        write(ctx, ServerTutorialFlagsMessage.DEFAULT)

        // SendInitialSpells
        write(ctx, ServerInitialSpellsMessage) // zero spell placeholder for now

        // SendInitialActionButtons
        write(ctx, ServerActionButtonsMessage) // zero action button ph for now

        // SendInitialReputations
        write(ctx, ServerInitialiseFactionsMessage) // zero faction ph for now

        // SETTIMESPEED
        write(ctx, ServerLoginSetTimeSpeedMessage(
                gameTime = 0x6140B211
        ))
    }

    private fun sendInitialPacketsAfterAddToMap(ctx: ChannelHandlerContext) {
        // UpdateZone

        //  ...SendInitWorldStates
        write(ctx, ServerInitWorldStatesMessage(
                mapId = 0,
                zoneId = Zone.DUN_MOROGH.zone))

    }

    companion object: KLogging()
}
