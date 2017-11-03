package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import net.jammos.gameserver.network.JammosGameAttributes.USERID_ATTRIBUTE
import net.jammos.gameserver.network.JammosGameAttributes.USERNAME_ATTRIBUTE
import net.jammos.utils.network.handler.JammosHandler

abstract class AuthenticatedGameHandler : JammosHandler() {
    fun username(ctx: ChannelHandlerContext) = ctx.channel().attr(USERNAME_ATTRIBUTE).get()
    fun userId(ctx: ChannelHandlerContext) = ctx.channel().attr(USERID_ATTRIBUTE).get()
}
