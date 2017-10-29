package net.jammos.gameserver.characters

import net.jammos.utils.auth.Username

class CharacterListManager {

    fun getCharacters(username: Username): Set<GameCharacter> {
        return setOf(
                GameCharacter(
                        guid = 123,
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
                        flags = 0,
                        isFirstLogin = false,
                        petId = 0,
                        petLevel = 0,
                        petFamily = 0)
        );

    }

}