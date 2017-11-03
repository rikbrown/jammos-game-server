package net.jammos.gameserver.network.message.coding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import mu.KLogging
import net.jammos.gameserver.network.JammosAttributes.CRYPTO_ATTRIBUTE
import net.jammos.gameserver.network.message.client.ClientMessage
import net.jammos.gameserver.network.message.crypto.MessageCrypto
import net.jammos.gameserver.network.message.crypto.NullMessageCrypto

class ClientMessageDecoder: ByteToMessageDecoder() {
    companion object : KLogging()

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        try {
            val crypto: MessageCrypto = ctx.channel().attr(CRYPTO_ATTRIBUTE).get()
                    ?: NullMessageCrypto

            val message = ClientMessage.read(buf, crypto)
            out.add(message)

            logger.debug { "Read message: $message" }

        } catch (e: Exception) {
            logger.error(e) { "Error decoding client message" }

        } finally {
            buf.clear()
        }
    }

}