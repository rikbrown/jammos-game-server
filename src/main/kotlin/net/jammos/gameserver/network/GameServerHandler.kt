package net.jammos.gameserver.network

import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.jammos.gameserver.network.message.server.ServerAuthChallengeMessage
import net.jammos.gameserver.workflow.step.AuthSessionStep
import net.jammos.utils.ByteArrays
import net.jammos.utils.network.WorkflowServerHandler

class GameServerHandler: WorkflowServerHandler(
        firstStep = AuthSessionStep()) {

    companion object: KLogging()

    private val seed = ByteArrays.randomBytes(4)

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)

        logger.info { "Channel active, writing auth challenge" }
        ctx.writeAndFlush(ServerAuthChallengeMessage(seed))
    }

}