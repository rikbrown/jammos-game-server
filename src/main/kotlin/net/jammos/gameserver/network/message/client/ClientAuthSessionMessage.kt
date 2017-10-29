package net.jammos.gameserver.network.message.client

import net.jammos.utils.auth.Username
import net.jammos.utils.auth.Username.Username.username
import net.jammos.utils.extensions.readBytes
import net.jammos.utils.extensions.readChars
import net.jammos.utils.extensions.readUnsignedIntLe
import net.jammos.utils.field
import net.jammos.utils.types.DigestByteArray
import java.io.DataInput

data class ClientAuthSessionMessage(
    val clientBuildNumber: Int,
    val serverId: Int,
    val accountName: Username,
    val clientSeed: ByteArray,
    val digest: DigestByteArray
): ClientMessage {

    companion object: ClientMessage.Reader<ClientAuthSessionMessage> {
        override fun readBody(input: DataInput): ClientAuthSessionMessage {
            return with (input) {
                // @formatter:off
                ClientAuthSessionMessage(
                        clientBuildNumber = field("clientBuildNumber") { readUnsignedIntLe() },
                        serverId          = field("serverId")          { readUnsignedIntLe() },
                        accountName       = field("accountName")       { username(readChars(reverse = false)) },
                        clientSeed        = field("clientSeed")        { readBytes(4) },
                        digest            = field("digest")            { DigestByteArray(readBytes(20)) }
                )
                // @formatter:on
            }
        }

    }

}