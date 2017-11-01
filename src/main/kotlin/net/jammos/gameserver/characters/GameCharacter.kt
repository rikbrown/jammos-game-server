package net.jammos.gameserver.characters

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING
import com.fasterxml.jackson.annotation.JsonValue
import net.jammos.utils.types.WriteableByte

data class GameCharacter(
        val id: CharacterId,
        val name: String, // string + 1 zero byte
        val race: Race, // byte
        val characterClass: CharacterClass, // byte
        val gender: Gender, // byte
        val skin: Skin, // byte
        val face: Face, // byte
        val hairStyle: HairStyle, // byte
        val hairColour: HairColour, // byte
        val facialHair: FacialHair, // byte
        val level: Int, // uint8
        val zone: Int, // uint32
        val map: Int, // uint32
        val x: Float, // 32bit
        val y: Float,
        val z: Float,
        val guildId: Int, // uint32
        val flags: Flags, // uint32
        val firstLogin: Boolean,
        val petId: Int, // uint32
        val petLevel: Int, // uint32,
        val petFamily: Int // uint32
) {
    data class Flags(
            val helmHidden: Boolean = false,
            val cloakHidden: Boolean = false,
            val ghost: Boolean = false,
            val mustRenameAtLogin: Boolean = false,
            val lockedForTransfer: Boolean = false,
            val lockedByBilling: Boolean = false,
            val declined: Boolean = false) {

        fun toInt(): Int {
            var flags = 0
            if (helmHidden) flags = flags or 0x00000400
            if (cloakHidden) flags = flags or 0x00000800
            if (ghost) flags = flags or 0x00002000
            if (mustRenameAtLogin) flags = flags or 0x00004000
            if (lockedForTransfer) flags = flags or 0x00000004
            if (lockedByBilling) flags = flags or 0x01000000
            if (declined) flags = flags or 0x02000000
            return flags
        }

    }
}

data class CharacterId @JsonCreator(mode = DELEGATING) constructor(@JsonValue val characterId: Long) {
    override fun toString() = characterId.toString()
}

enum class Race(override val value: Int): WriteableByte {
    Orc(2)
}

enum class CharacterClass(override val value: Int): WriteableByte {
    Warrior(1)
}

enum class Gender(override val value: Int): WriteableByte {
    Male(0)
}

enum class Skin(override val value: Int): WriteableByte {
    Dunno(1)
}

enum class Face(override val value: Int): WriteableByte {
    Dunno(8)
}

enum class HairStyle(override val value: Int): WriteableByte {
    Dunno(3)
}

enum class HairColour(override val value: Int): WriteableByte {
    Dunno(0)
}

enum class FacialHair(override val value: Int): WriteableByte {
    Dunno(5)
}