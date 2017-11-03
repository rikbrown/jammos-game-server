package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf
import net.jammos.utils.auth.Username
import net.jammos.utils.auth.Username.Username.username
import net.jammos.utils.extensions.readByteArray
import net.jammos.utils.extensions.readChars
import net.jammos.utils.field
import net.jammos.utils.types.DigestByteArray

data class ClientAuthSessionMessage(
    val clientBuildNumber: Long,
    val serverId: Long,
    val accountName: Username,
    val clientSeed: ByteArray,
    val digest: DigestByteArray
): ClientMessage {

    companion object: ClientMessage.Reader<ClientAuthSessionMessage> {
        override fun readBody(input: ByteBuf): ClientAuthSessionMessage {
            return with (input) {
                // @formatter:off
                ClientAuthSessionMessage(
                        clientBuildNumber = field("clientBuildNumber") { readUnsignedIntLE() },
                        serverId          = field("serverId")          { readUnsignedIntLE() },
                        accountName       = field("accountName")       { username(readChars(reverse = false)) },
                        clientSeed        = field("clientSeed")        { readByteArray(4) },
                        digest            = field("digest")            { DigestByteArray(readByteArray(20)) }
                )
                // @formatter:on
            }
        }

    }

}