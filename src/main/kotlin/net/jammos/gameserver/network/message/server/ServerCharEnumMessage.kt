package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import mu.KLogging
import net.jammos.gameserver.characters.GameCharacter
import net.jammos.gameserver.network.ServerCommand.CHAR_ENUM
import net.jammos.utils.extensions.writeByte
import net.jammos.utils.extensions.writeCharSequenceTerminated
import net.jammos.utils.extensions.writeFloatLE

data class ServerCharEnumMessage(val characters: Set<GameCharacter>): ServerMessage(CHAR_ENUM) {
    companion object: KLogging()

    // structure: 1b [char count (UInt8)] + ...
    override val size = 1 + characters.sumBy { 159 + it.name.length }

    override fun writeData(output: ByteBuf) {
        with (output) {
            // FIXME: rethink all this
            writeByte(characters.size) // character count

            characters.forEach { character ->
                writeLongLE(character.id.characterId)
                writeCharSequenceTerminated(character.name)
                writeByte(character.race)
                writeByte(character.characterClass)
                writeByte(character.gender)
                writeByte(character.skin)
                writeByte(character.face)
                writeByte(character.hairStyle)
                writeByte(character.hairColour)
                writeByte(character.facialHair)
                writeByte(character.level)
                writeIntLE(character.zone)
                writeIntLE(character.map)
                writeFloatLE(character.x)
                writeFloatLE(character.y)
                writeFloatLE(character.z)
                writeIntLE(character.guildId)
                writeIntLE(character.flags.toInt())
                writeBoolean(character.firstLogin)
                writeIntLE(character.petId)
                writeIntLE(character.petLevel)
                writeIntLE(character.petFamily)

                // Equipment slots
                for (i in 1..19) {
                    writeIntLE(0)
                    writeByte(0)
                }

                // First bag
                writeIntLE(0)
                writeByte(0)
            }
        }
    }

}
