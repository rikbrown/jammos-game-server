package net.jammos.gameserver.network.message.client

import net.jammos.utils.extensions.readBytes
import net.jammos.utils.extensions.readChars
import net.jammos.utils.extensions.readUnsignedInt
import net.jammos.utils.field
import java.io.DataInput

data class ClientAuthSessionMessage(
    val clientBuildNumber: Int,
    val serverId: Int,
    val accountName: String,
    val clientSeed: ByteArray,
    val digest: ByteArray
): ClientMessage {

    companion object: ClientMessage.Reader<ClientAuthSessionMessage> {
        override fun readBody(input: DataInput): ClientAuthSessionMessage {
            return with (input) {
                // @formatter:off
                ClientAuthSessionMessage(
                        clientBuildNumber = field("clientBuildNumber") { readUnsignedInt() },
                        serverId          = field("serverId")          { readUnsignedInt() },
                        accountName       = field("accountName")       { readChars() },
                        clientSeed        = field("clientSeed")        { readBytes(4) },
                        digest            = field("digest")            { readBytes(20) }
                )
                // @formatter:on
            }
        }

    }

}