package net.jammos.gameserver.workflow.step

import net.jammos.gameserver.network.message.client.ClientAuthSessionMessage
import net.jammos.gameserver.network.message.server.ServerAuthChallengeMessage
import net.jammos.utils.workflow.Step

class AuthSessionStep : Step<ClientAuthSessionMessage, ServerAuthChallengeMessage>(ClientAuthSessionMessage::class) {

    override fun handle0(msg: ClientAuthSessionMessage): ResponseAndNextStep<ServerAuthChallengeMessage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}