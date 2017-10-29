package net.jammos.gameserver.characters

import net.jammos.utils.types.WriteableByte

data class GameCharacter(
        val guid: Long,
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
        val flags: Int, // uint32 - TODO
        val isFirstLogin: Boolean,
        val petId: Int, // uint32
        val petLevel: Int, // uint32,
        val petFamily: Int // uint32
)

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