package net.jammos.gameserver.network.message.coding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import mu.KLogging
import net.jammos.gameserver.network.message.server.ServerMessage
import net.jammos.utils.extensions.asDataOutput

class ServerMessageEncoder : MessageToByteEncoder<ServerMessage>() {
    companion object : KLogging()

    override fun encode(ctx: ChannelHandlerContext, msg: ServerMessage, out: ByteBuf) {
        try {
            logger.info { "Writing message: $msg" }
            msg.write(out.asDataOutput())

        } catch (e: Exception) {
            logger.error(e) { "Error encoding output: $msg" }

        }
    }
}