package net.jammos.gameserver.characters

import net.jammos.utils.auth.Username

class CharacterListManager {

    fun getCharacters(username: Username): Set<GameCharacter> {
        return setOf(
                GameCharacter(
                        guid = 1,
                        name = "Rikalorgh",
                        race = Race.Orc,
                        characterClass = CharacterClass.Warrior,
                        gender = Gender.Male,
                        skin = Skin.Dunno,
                        face = Face.Dunno,
                        hairStyle = HairStyle.Dunno,
                        hairColour = HairColour.Dunno,
                        facialHair = FacialHair.Dunno,
                        level = 60,
                        zone = 0,
                        map = 0,
                        x = 0F,
                        y = 0F,
                        z = 0F,
                        guildId = 0,
                        flags = GameCharacter.Flags(
                                isGhost = true
                        ),
                        isFirstLogin = false,
                        petId = 0,
                        petLevel = 0,
                        petFamily = 0),
                GameCharacter(
                        guid = 2,
                        name = "Mazornus",
                        race = Race.Orc,
                        characterClass = CharacterClass.Warrior,
                        gender = Gender.Male,
                        skin = Skin.Dunno,
                        face = Face.Dunno,
                        hairStyle = HairStyle.Dunno,
                        hairColour = HairColour.Dunno,
                        facialHair = FacialHair.Dunno,
                        level = 60,
                        zone = 0,
                        map = 0,
                        x = 0F,
                        y = 0F,
                        z = 0F,
                        guildId = 0,
                        flags = GameCharacter.Flags(),
                        isFirstLogin = false,
                        petId = 0,
                        petLevel = 0,
                        petFamily = 0))

    }

}