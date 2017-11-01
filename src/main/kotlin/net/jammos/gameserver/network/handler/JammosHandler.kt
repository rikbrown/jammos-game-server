package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import mu.KLogging
import net.jammos.gameserver.network.JammosAttributes.USERID_ATTRIBUTE
import net.jammos.gameserver.network.JammosAttributes.USERNAME_ATTRIBUTE

abstract class JammosHandler: ChannelInboundHandlerAdapter() {
    companion object: KLogging()

    override abstract fun channelRead(ctx: ChannelHandlerContext, msg: Any)

    fun pass(ctx: ChannelHandlerContext, msg: Any) {
        logger.debug { "$javaClass passing on: $msg" }
        ctx.fireChannelRead(msg)
    }

    fun finalResponse(ctx: ChannelHandlerContext, msg: Any) {
        respond(ctx, msg)
        closeSession(ctx)
    }

    fun respond(ctx: ChannelHandlerContext, msg: Any) {
        logger.debug { "Responding with: $msg" }
        ctx.writeAndFlush(msg)
    }

    fun closeSession(ctx: ChannelHandlerContext) {
        logger.info { "Closing session" }
        // If we don't specify this, it seems like we close too quickly
        Thread.sleep(500)
        ctx.close()
    }

    @Suppress("OverridingDeprecatedMember") // deprecation warning is only on one base class
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.error(cause) { "Caught exception, disconnecting" }
        ctx.disconnect()
        ctx.close()
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        logger.info { "Channel inactive" }
    }
}

abstract class AuthenticatedJammosHandler: JammosHandler() {
    fun username(ctx: ChannelHandlerContext) = ctx.channel().attr(USERNAME_ATTRIBUTE).get()
    fun userId(ctx: ChannelHandlerContext) = ctx.channel().attr(USERID_ATTRIBUTE).get()
}