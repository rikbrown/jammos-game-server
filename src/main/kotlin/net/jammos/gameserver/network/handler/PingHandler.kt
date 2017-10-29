package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import net.jammos.gameserver.network.message.client.ClientPingMessage
import net.jammos.gameserver.network.message.server.ServerPongMessage

@ChannelHandler.Sharable
object PingHandler: JammosHandler() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientPingMessage) return pass(ctx, msg)

        // TODO: check for overspeed and kick

        // Return the ping as a pong!
        respond(ctx, ServerPongMessage(msg.ping))
    }
}
