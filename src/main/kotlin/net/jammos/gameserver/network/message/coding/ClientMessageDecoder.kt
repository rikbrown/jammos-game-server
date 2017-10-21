package net.jammos.gameserver.network.message.coding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import mu.KLogging
import net.jammos.gameserver.network.message.client.ClientMessage
import net.jammos.utils.extensions.asDataInput

class ClientMessageDecoder: ByteToMessageDecoder() {
    companion object : KLogging()

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        try {
            val message = ClientMessage.read(buf.asDataInput())
            logger.debug { "Read message: $message" }
            out.add(message)

        } catch (e: Exception) {
            logger.warn(e) { "Error decoding client message"}

        } finally {
            buf.clear()
        }
    }

}