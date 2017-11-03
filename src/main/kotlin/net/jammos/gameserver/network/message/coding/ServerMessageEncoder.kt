package net.jammos.gameserver.network.message.coding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import mu.KLogging
import net.jammos.gameserver.network.JammosAttributes.CRYPTO_ATTRIBUTE
import net.jammos.gameserver.network.message.server.ServerMessage

class ServerMessageEncoder : MessageToByteEncoder<ServerMessage>() {
    companion object : KLogging()

    override fun encode(ctx: ChannelHandlerContext, msg: ServerMessage, out: ByteBuf) {
        try {
            val crypto = ctx.channel().attr(CRYPTO_ATTRIBUTE).get()

            logger.info { "Writing message: $msg" }
            msg.write(out, crypto)

        } catch (e: Exception) {
            logger.error(e) { "Error encoding output: $msg" }

        }
    }
}