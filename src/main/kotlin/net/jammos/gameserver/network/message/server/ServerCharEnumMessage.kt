package net.jammos.gameserver.network.message.server

import io.netty.buffer.ByteBuf
import mu.KLogging
import net.jammos.gameserver.characters.GameCharacter
import net.jammos.gameserver.network.ServerCommand.CHAR_ENUM
import net.jammos.utils.extensions.writeByte
import net.jammos.utils.extensions.writeCharSequenceTerminated

data class ServerCharEnumMessage(val characters: Set<GameCharacter>): ServerMessage(CHAR_ENUM) {
    companion object: KLogging()

    // structure: 1b [char count (UInt8)] + ...
    override val size = 1 + characters.sumBy { 159 + it.name.length }

    override fun writeData(output: ByteBuf) {
        with (output) {
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
                writeIntLE(character.zone.zone)
                writeIntLE(character.map)
                writeFloatLE(character.position.x)
                writeFloatLE(character.position.y)
                writeFloatLE(character.position.z)
                writeIntLE(character.guildId)
                writeIntLE(character.flags.intValue)
                writeBoolean(character.firstLogin)
                writeIntLE(character.pet?.id ?: 0)
                writeIntLE(character.pet?.level ?: 0)
                writeIntLE(character.pet?.family ?: 0)

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
