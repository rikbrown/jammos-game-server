package net.jammos.gameserver.network.message.client

import net.jammos.gameserver.characters.CharacterClass
import net.jammos.gameserver.characters.Gender
import net.jammos.gameserver.characters.Race
import net.jammos.gameserver.network.message.client.ClientMessage.Reader
import net.jammos.utils.extensions.readChars
import net.jammos.utils.field
import java.io.DataInput

data class ClientCharCreateMessage(
        val name: String, // string + 1 zero byte
        val race: Race, // byte
        val characterClass: CharacterClass, // byte
        val gender: Gender, // byte
        val skin: Int, // byte
        val face: Int, // byte
        val hairStyle: Int, // byte
        val hairColour: Int, // byte
        val facialHair: Int, // byte
        val outfitId: Int // byte
): ClientMessage {
    companion object: Reader<ClientCharCreateMessage> {
        override fun readBody(input: DataInput) = with (input) {
            // @formatter:off
            ClientCharCreateMessage(
                    name = field("name")             { readChars(reverse = false) },
                    race = field("race")             { Race.ofValue(readUnsignedByte()) },
                    characterClass = field("class")  { CharacterClass.ofValue(readUnsignedByte()) },
                    gender = field("gender")         { Gender.ofValue(readUnsignedByte()) },
                    skin = field("skin")             { readUnsignedByte() },
                    face = field("face")             { readUnsignedByte() },
                    hairStyle = field("hairStyle")   { readUnsignedByte() },
                    hairColour = field("hairColour") { readUnsignedByte() },
                    facialHair = field("facialHair") { readUnsignedByte() },
                    outfitId = field("outfitId")     { readUnsignedByte() })
            // @formatter:on
        }
    }
}