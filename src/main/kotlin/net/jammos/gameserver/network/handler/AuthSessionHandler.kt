package net.jammos.gameserver.network.handler

import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.jammos.gameserver.auth.SessionAuthValidator
import net.jammos.gameserver.auth.SessionValidationFailure
import net.jammos.gameserver.auth.SuspendedException
import net.jammos.gameserver.auth.UnknownUserException
import net.jammos.gameserver.network.JammosAttributes.CRYPTO_ATTRIBUTE
import net.jammos.gameserver.network.JammosAttributes.USERNAME_ATTRIBUTE
import net.jammos.gameserver.network.ResponseCode.*
import net.jammos.gameserver.network.message.client.ClientAuthSessionMessage
import net.jammos.gameserver.network.message.crypto.DefaultMessageCrypto
import net.jammos.gameserver.network.message.crypto.NullMessageCrypto
import net.jammos.gameserver.network.message.server.ServerAuthChallengeMessage
import net.jammos.gameserver.network.message.server.ServerAuthResponseMessage
import net.jammos.utils.ByteArrays

class AuthSessionHandler(private val authValidator: SessionAuthValidator): JammosHandler() {
    companion object: KLogging()

    /**
     * Unique seed for this session
     */
    private val seed: ByteArray = ByteArrays.randomSeed(4)

    /**
     * When channel opens, respond with an auth challenge
     */
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        logger.info { "Channel active, writing auth challenge" }

        // Crypto is null by default
        ctx.channel().attr(CRYPTO_ATTRIBUTE).set(NullMessageCrypto)

        // Write auth challenge
        ctx.writeAndFlush(ServerAuthChallengeMessage(seed))
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg !is ClientAuthSessionMessage) return pass(ctx, msg)

        // Attempt to validate session and retrieve session key
        val sessionKey = try {
            authValidator.validateSession(msg.accountName, seed, msg.clientSeed, msg.digest)

        } catch (e: UnknownUserException) {
            logger.info { "Unknown user attempted logon: ${msg.accountName}" }
            return finalResponse(ctx, ServerAuthResponseMessage(AUTH_UNKNOWN_ACCOUNT))

        } catch (e: SuspendedException) {
            logger.info { "Suspended user attempted logon by ${msg.accountName} - ${e.message}" }
            return finalResponse(ctx, ServerAuthResponseMessage(AUTH_BANNED))

        } catch (e: SessionValidationFailure) {
            // any other failure (like a key mismatch)
            logger.info(e) { "Session validation failure: ${msg.accountName}" }
            return finalResponse(ctx, ServerAuthResponseMessage(AUTH_FAILED))
        }

        // From here on out, message headers are encrypted using this session key
        // Put it into the request context for this channel so encoder/decoder can use it
        ctx.channel().attr(CRYPTO_ATTRIBUTE).set(DefaultMessageCrypto(sessionKey))

        // Also we're authenticated, so save the username
        ctx.channel().attr(USERNAME_ATTRIBUTE).set(msg.accountName)

        // Respond with an OK!
        respond(ctx, ServerAuthResponseMessage(AUTH_OK, ServerAuthResponseMessage.SuccessData()))
    }

}

