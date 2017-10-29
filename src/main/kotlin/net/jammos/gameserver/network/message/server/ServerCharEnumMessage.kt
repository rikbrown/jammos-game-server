package net.jammos.gameserver.network.message.server

import com.google.common.io.LittleEndianDataOutputStream
import mu.KLogging
import net.jammos.gameserver.characters.GameCharacter
import net.jammos.gameserver.network.ServerCommand.CHAR_ENUM
import net.jammos.utils.extensions.writeCharsWithTerminator
import java.io.DataOutput
import java.io.OutputStream

data class ServerCharEnumMessage(val characters: Set<GameCharacter>): ServerMessage(CHAR_ENUM) {
    companion object: KLogging()

    // structure: 1b [char count (UInt8)]
    override val size = 1 + 159 + "Rikalorgh".length

    override fun writeData(output: DataOutput) {
        val leOutput = LittleEndianDataOutputStream(output as OutputStream) // FIXME: rethink all this

        leOutput.writeByte(characters.size) // character count

        characters.forEach { character ->
            leOutput.writeLong(character.guid)
            leOutput.writeCharsWithTerminator(character.name)
            character.race.write(leOutput)
            character.characterClass.write(leOutput)
            character.gender.write(leOutput)
            character.skin.write(leOutput)
            character.face.write(leOutput)
            character.hairStyle.write(leOutput)
            character.hairColour.write(leOutput)
            character.facialHair.write(leOutput)
            leOutput.writeByte(character.level)
            leOutput.writeInt(character.zone)
            leOutput.writeInt(character.map)
            leOutput.writeFloat(character.x)
            leOutput.writeFloat(character.y)
            leOutput.writeFloat(character.z)
            leOutput.writeInt(character.guildId)
            leOutput.writeInt(character.flags)
            leOutput.writeBoolean(character.isFirstLogin)
            leOutput.writeInt(character.petId)
            leOutput.writeInt(character.petLevel)
            leOutput.writeInt(character.petFamily)

            for (i in 0..19) {
                leOutput.writeInt(0)
                leOutput.writeByte(0)
            }

            leOutput.writeInt(0)
            leOutput.writeByte(0)
        }
    }

}
