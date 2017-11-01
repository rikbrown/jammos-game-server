package net.jammos.gameserver.network

import io.netty.util.AttributeKey
import net.jammos.gameserver.network.message.crypto.MessageCrypto
import net.jammos.utils.auth.UserId
import net.jammos.utils.auth.Username

object JammosAttributes {
    val USERID_ATTRIBUTE = AttributeKey.valueOf<UserId>("userId")!!
    val USERNAME_ATTRIBUTE = AttributeKey.valueOf<Username>("username")!!
    val CRYPTO_ATTRIBUTE = AttributeKey.valueOf<MessageCrypto>("crypto")!!
}