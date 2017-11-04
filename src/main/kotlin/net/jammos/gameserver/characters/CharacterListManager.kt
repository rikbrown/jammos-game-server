package net.jammos.gameserver.characters

import mu.KLogging
import net.jammos.gameserver.Position
import net.jammos.gameserver.zones.Zone
import net.jammos.utils.auth.UserId

class CharacterListManager(private val characterDao: CharacterDao) {

    fun createCharacter(
            userId: UserId,
            name: String,
            race: Race,
            characterClass: CharacterClass,
            gender: Gender,
            skin: Short,
            face: Short,
            hairStyle: Short,
            hairColour: Short,
            facialHair: Short): GameCharacter {

        // Create character
        return characterDao.createCharacter(GameCharacter(
                userId = userId,
                name = name,
                race = race,
                characterClass = characterClass,
                gender = gender,
                skin = skin,
                face = face,
                hairStyle = hairStyle,
                hairColour = hairColour,
                facialHair = facialHair,
                zone = Zone.NONE, // TODO
                map = 0, // TODO
                position = Position.ZERO, // TODO
                firstLogin = true))
    }

    fun deleteCharacter(userId: UserId, characterId: CharacterId) {
        // Get the character
        val character = getCharacter(characterId)
                ?.takeIf { it.userId == userId } // (if it belongs to current user)
                ?: return logger.warn { "Attempted to delete character $characterId: non-existent or not belonging to user $userId" }

        // TODO: nope out if current player
        // TODO: nope if guild leader
        // TODO: logout player if online
        characterDao.deleteCharacter(character)
    }

    fun getCharacters(userId: UserId) = characterDao.listCharacters(userId)
    private fun getCharacter(characterId: CharacterId) = characterDao.getCharacter(characterId)


    companion object: KLogging()
}