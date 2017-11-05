package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import net.jammos.gameserver.network.message.client.ClientLoginMessage
import net.jammos.gameserver.network.message.server.ServerAccountDataTimesMessage
import net.jammos.gameserver.network.message.server.ServerVerifyWorldMessage

class VerifyWorldHandler: AuthenticatedGameHandler() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientLoginMessage) return pass(ctx, msg)

        respond(ctx, ServerVerifyWorldMessage(0, 0F, 0F, 0F, 0F))
        Thread.sleep(500)
        respond(ctx, ServerAccountDataTimesMessage)
    }

}
