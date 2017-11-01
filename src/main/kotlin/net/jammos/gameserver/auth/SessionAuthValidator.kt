package net.jammos.gameserver.auth

import mu.KLogging
import net.jammos.utils.auth.UserId
import net.jammos.utils.auth.Username
import net.jammos.utils.auth.crypto.CryptoManager
import net.jammos.utils.auth.dao.AuthDao
import net.jammos.utils.extensions.digestByteArray
import net.jammos.utils.extensions.update
import net.jammos.utils.types.BigUnsignedInteger
import net.jammos.utils.types.DigestByteArray
import java.net.InetAddress

/**
 * TODO:
 * - IP locks
 * - server security levels
 */
class SessionAuthValidator(
        private val authDao: AuthDao,
        private val cryptoManager: CryptoManager) {

    companion object: KLogging() {
        private val FOUR_ZERO_BYTES = byteArrayOf(0, 0, 0, 0)
    }

    fun validateSession(username: Username, serverSeed: ByteArray, clientSeed: ByteArray, clientDigest: DigestByteArray): UserIdAndSessionKey {
        // 0. Get user ID
        val userId = authDao.getUserAuth(username)?.userId ?: throw UnknownUserException(username)

        // 1. Validate client version
        // TODO

        // 2. Verify user is not banned
        // FIXME: ignores IP bans - work out how to plumb IP through
        authDao.getUserSuspension(userId)
                ?.let { it.end != null }
                ?.let { throw UserSuspendedException(username, temporary = it) }

        // 3. Validate session key (and quit now if the user isn't recognised)
        return UserIdAndSessionKey(userId,
                sessionKey = validateSessionKey(userId, username, serverSeed, clientSeed, clientDigest))
    }

    private fun validateSessionKey(userId: UserId, username: Username, serverSeed: ByteArray, clientSeed: ByteArray, clientDigest: DigestByteArray): BigUnsignedInteger {
        val sessionKey = authDao.getUserSessionKey(userId) ?: throw NoUserSessionException(username)

        val digest = with(cryptoManager.sha1()) {
            update(username.bytes)
            update(FOUR_ZERO_BYTES)
            update(clientSeed)
            update(serverSeed)
            update(sessionKey)
            digestByteArray()
        }

        if (digest != clientDigest) {
            throw KeyMismatchFailure(username)
        }

        return sessionKey
    }

}

data class UserIdAndSessionKey(val userId: UserId, val sessionKey: BigUnsignedInteger)
sealed class SessionValidationFailure(msg: String): RuntimeException(msg)
class UnknownUserException(username: Username): SessionValidationFailure("Unknown username: $username")
sealed class SuspendedException(message: String, val temporary: Boolean): SessionValidationFailure("$message (temporary=$temporary)")
class IpBannedException(ip: InetAddress, temporary: Boolean): SuspendedException("IP banned: $ip", temporary)
class UserSuspendedException(username: Username, temporary: Boolean): SuspendedException("User suspended: $username", temporary)
class KeyMismatchFailure(username: Username): SessionValidationFailure("Server/client digest mismatch for username: $username")
class NoUserSessionException(username: Username): SessionValidationFailure("No session found for username: $username")
