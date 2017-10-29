package net.jammos.gameserver.network

import io.netty.util.AttributeKey
import net.jammos.gameserver.network.message.crypto.MessageCrypto
import net.jammos.utils.auth.Username

object JammosAttributes {
    val USERNAME_ATTRIBUTE = AttributeKey.valueOf<Username>("username")!!
    val CRYPTO_ATTRIBUTE = AttributeKey.valueOf<MessageCrypto>("crypto")!!
}