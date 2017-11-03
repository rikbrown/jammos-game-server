package net.jammos.gameserver.network.message.client

import io.netty.buffer.ByteBuf
import net.jammos.gameserver.characters.CharacterClass
import net.jammos.gameserver.characters.Gender
import net.jammos.gameserver.characters.Race
import net.jammos.gameserver.network.message.client.ClientMessage.Reader
import net.jammos.utils.extensions.readCharsUntil
import net.jammos.utils.field

data class ClientCharCreateMessage(
        val name: String, // string + 1 zero byte
        val race: Race, // byte
        val characterClass: CharacterClass, // byte
        val gender: Gender, // byte
        val skin: Short, // byte
        val face: Short, // byte
        val hairStyle: Short, // byte
        val hairColour: Short, // byte
        val facialHair: Short, // byte
        val outfitId: Short // byte
): ClientMessage {
    companion object: Reader<ClientCharCreateMessage> {
        override fun readBody(input: ByteBuf) = with (input) {
            // @formatter:off
            ClientCharCreateMessage(
                    name = field("name")             { readCharsUntil(0) },
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