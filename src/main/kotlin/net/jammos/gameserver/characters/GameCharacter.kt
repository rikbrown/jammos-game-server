package net.jammos.gameserver.characters

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING
import com.fasterxml.jackson.annotation.JsonValue
import net.jammos.gameserver.characters.CharacterId.Companion.UNDEFINED
import net.jammos.utils.auth.UserId
import net.jammos.utils.types.ReversibleByte
import net.jammos.utils.types.WriteableByte

data class GameCharacter(
        val id: CharacterId = UNDEFINED,
        val userId: UserId,
        val name: String, // string + 1 zero byte
        val race: Race, // byte
        val characterClass: CharacterClass, // byte
        val gender: Gender, // byte
        val skin: Int, // byte
        val face: Int, // byte
        val hairStyle: Int, // byte
        val hairColour: Int, // byte
        val facialHair: Int, // byte
        val level: Int = 1, // uint8
        val zone: Int, // uint32
        val map: Int, // uint32
        val x: Float, // 32bit
        val y: Float,
        val z: Float,
        val guildId: Int = 0, // uint32
        val flags: Flags = Flags(), // uint32
        val firstLogin: Boolean,
        val petId: Int = 0, // uint32
        val petLevel: Int = 0, // uint32,
        val petFamily: Int = 0 // uint32
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

    companion object {
        val UNDEFINED = CharacterId(0)
    }
}


enum class Race(override val value: Int): WriteableByte {
    Human(1),
    Orc(2),
    Dwarf(3),
    NightElf(4),
    Undead(5),
    Tauren(6),
    Gnome(7),
    Troll(8),
    Goblin(9);

    companion object: ReversibleByte<Race>(values())
}

enum class CharacterClass(override val value: Int): WriteableByte {
    Warrior(1),
    Hunter(3);

    companion object: ReversibleByte<CharacterClass>(values())
}

enum class Gender(override val value: Int): WriteableByte {
    Male(0),
    Female(1);

    companion object: ReversibleByte<Gender>(values())
}

