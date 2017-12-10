package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.network.ServerCommand.LOGIN_SETTIMESPEED

// TODO: actually send some factions
data class ServerLoginSetTimeSpeedMessage(
        val gameTime: Int,
        val gameSpeed: Float = 0.01666667F): ServerMessage(LOGIN_SETTIMESPEED) {
    override val size =  4 + 4

    override fun writeData(output: ByteBuf) {
        with(output) {
            writeInt(gameTime)
            writeFloatLE(gameSpeed)
        }
    }
}


