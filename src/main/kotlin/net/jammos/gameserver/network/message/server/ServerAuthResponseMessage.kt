package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.AUTH_RESPONSE
import net.jammos.utils.extensions.writeByte
import net.jammos.utils.types.WriteableByte

data class ServerAuthResponseMessage(
        val response: AuthResponseCode,
        val successData: SuccessData? = null): ServerMessage(AUTH_RESPONSE) {
    override val size = 1 + (successData?.size ?: 0)

    override fun writeData(output: ByteBuf) {
        output.writeByte(response)
        successData?.write(output)
    }

    data class SuccessData(
            val billingTimeRemaining: Int = 0,
            val billingPlanFlags: Int = 0,
            val billingTimeRested: Int = 0) {

        val size = 4 + 1 + 4

        fun write(output: ByteBuf) {
            output.writeIntLE(billingTimeRemaining)
            output.writeByte(billingPlanFlags)
            output.writeIntLE(billingTimeRested)
        }
    }

    enum class AuthResponseCode(override val value: Short): WriteableByte {
        AUTH_OK(0x0C),
        AUTH_UNKNOWN_ACCOUNT(0x15),
        AUTH_BANNED(0x1C),
        AUTH_FAILED(0x0D)
    }
}

