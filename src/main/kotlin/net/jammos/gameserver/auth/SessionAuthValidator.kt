package net.jammos.gameserver.auth

import mu.KLogging
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

    fun validateSession(username: Username, serverSeed: ByteArray, clientSeed: ByteArray, clientDigest: DigestByteArray): BigUnsignedInteger {
        // 1. Validate client version

        // 2. Verify user is not banned
        // FIXME: ignores IP bans - work out how to plumb IP through
        authDao.getUserSuspension(username)
                ?.let { it.end != null }
                ?.let { throw UserSuspendedException(username, temporary = it) }

        // 3. Validate session key (and quit now if the user isn't recognised)
        return validateSessionKey(username, serverSeed, clientSeed, clientDigest)
    }

    private fun validateSessionKey(username: Username, serverSeed: ByteArray, clientSeed: ByteArray, clientDigest: DigestByteArray): BigUnsignedInteger {
        val sessionKey = authDao.getUserSessionKey(username)
                ?: throw UnknownUserException(username)

        val digest = with(cryptoManager.sha1()) {
            update(username.bytes)
            update(FOUR_ZERO_BYTES)
            update(clientSeed)
            update(serverSeed)
            update(sessionKey)
            digestByteArray()
        }

        if (digest != clientDigest) {
            throw KeyMismatchFailure()
        }

        return sessionKey
    }

}

sealed class SessionValidationFailure(msg: String): RuntimeException(msg)
class UnknownUserException(username: Username): SessionValidationFailure("Unknown username: $username")
sealed class SuspendedException(message: String, val temporary: Boolean): SessionValidationFailure("$message (temporary=$temporary)")
class IpBannedException(ip: InetAddress, temporary: Boolean): SuspendedException("IP banned: $ip", temporary)
class UserSuspendedException(username: Username, temporary: Boolean): SuspendedException("User suspended: $username", temporary)
class KeyMismatchFailure: SessionValidationFailure("Server/client digest mismatch")
