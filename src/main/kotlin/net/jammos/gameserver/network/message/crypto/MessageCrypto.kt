package net.jammos.gameserver.network.message.crypto

import net.jammos.utils.types.BigUnsignedInteger
import kotlin.experimental.xor

const private val CRYPTED_SEND_LEN = 4

interface MessageCrypto {
    fun encrypt(data: ByteArray): ByteArray
    fun decrypt(data: ByteArray): ByteArray
}

object NullMessageCrypto: MessageCrypto {
    override fun encrypt(data: ByteArray) = data
    override fun decrypt(data: ByteArray) = data
}

class DefaultMessageCrypto(sessionKey: BigUnsignedInteger): MessageCrypto {
    private val keyBytes = sessionKey.bytes

    private var sendI: Byte = 0
    private var sendJ: Byte = 0
    private var recvI: Byte = 0
    private var recvJ: Byte = 0

    override fun encrypt(data: ByteArray): ByteArray {
        for (t in data.indices) {
            sendI = (sendI % keyBytes.size).toByte()
            val x = ((data[t] xor keyBytes[sendI.toInt()]) + sendJ).toByte()
            ++sendI
            sendJ = x
            data[t] = sendJ
        }
        return data
    }

    override fun decrypt(data: ByteArray): ByteArray {
        for (t in data.indices) {
            recvI = (recvI % keyBytes.size).toByte()
            val x = (data[t] - recvJ xor keyBytes[recvI.toInt()].toInt()).toByte()
            ++recvI
            recvJ = data[t]
            data[t] = x
        }

        return data
    }

}