package net.jammos.gameserver.network.message.server

import com.google.common.io.LittleEndianDataOutputStream
import mu.KLogging
import net.jammos.gameserver.characters.GameCharacter
import net.jammos.gameserver.network.ServerCommand.CHAR_ENUM
import net.jammos.utils.extensions.writeByte
import net.jammos.utils.extensions.writeCharsTerminated
import java.io.DataOutput
import java.io.OutputStream

data class ServerCharEnumMessage(val characters: Set<GameCharacter>): ServerMessage(CHAR_ENUM) {
    companion object: KLogging()

    // structure: 1b [char count (UInt8)] + ...
    override val size = 1 + characters.sumBy { 159 + it.name.length }

    override fun writeData(output: DataOutput) {
        with (LittleEndianDataOutputStream(output as OutputStream)) {
            // FIXME: rethink all this
            writeByte(characters.size) // character count

            characters.forEach { character ->
                writeLong(character.id.characterId)
                writeCharsTerminated(character.name)
                writeByte(character.race)
                writeByte(character.characterClass)
                writeByte(character.gender)
                write(character.skin)
                write(character.face)
                write(character.hairStyle)
                write(character.hairColour)
                write(character.facialHair)
                writeByte(character.level)
                writeInt(character.zone)
                writeInt(character.map)
                writeFloat(character.x)
                writeFloat(character.y)
                writeFloat(character.z)
                writeInt(character.guildId)
                writeInt(character.flags.toInt())
                writeBoolean(character.firstLogin)
                writeInt(character.petId)
                writeInt(character.petLevel)
                writeInt(character.petFamily)

                // Equipment slots
                for (i in 1..19) {
                    writeInt(0)
                    writeByte(0)
                }

                // First bag
                writeInt(0)
                writeByte(0)
            }
        }
    }

}
