package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.jammos.gameserver.network.JammosAttributes.USERNAME_ATTRIBUTE

class RequireAuthenticationHandler: JammosHandler() {
    companion object: KLogging()

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (!ctx.channel().hasAttr(USERNAME_ATTRIBUTE)) {
            logger.warn { "Client is not authenticated but authentication required for: $msg" }
            return closeSession(ctx)
        }

        pass(ctx, msg)
    }

}