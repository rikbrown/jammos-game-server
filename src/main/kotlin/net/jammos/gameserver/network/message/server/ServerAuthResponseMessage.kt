package net.jammos.gameserver.network.message.server

import net.jammos.gameserver.network.ResponseCode
import net.jammos.gameserver.network.ServerCommand.AUTH_RESPONSE
import java.io.DataOutput

data class ServerAuthResponseMessage(
        val response: ResponseCode,
        val successData: SuccessData? = null): ServerMessage(AUTH_RESPONSE) {
    override val size = 1 + (successData?.size ?: 0)

    override fun writeData(output: DataOutput) {
        response.write(output)
        successData?.write(output)
    }

    data class SuccessData(
            val billingTimeRemaining: Int = 0,
            val billingPlanFlags: Int = 0,
            val billingTimeRested: Int = 0) {

        val size = 4 + 1 + 4

        fun write(output: DataOutput) {
            output.writeInt(billingTimeRemaining)
            output.writeByte(billingPlanFlags)
            output.writeInt(billingTimeRested)
        }
    }
}