package net.jammos.gameserver.characters

import mu.KLogging
import net.jammos.gameserver.Position
import net.jammos.gameserver.characters.CharacterListManager.CharacterCreationFailure.*
import net.jammos.gameserver.characters.CharacterListManager.CharacterCreationFailure.IllegalCharacterName.*
import net.jammos.gameserver.config.ConfigManager
import net.jammos.gameserver.zones.Zone
import net.jammos.utils.auth.UserId
import net.jammos.utils.realm.RealmDao
import net.jammos.utils.realm.RealmId

const private val MAX_NAME_LENGTH = 12 // max allowed by client
const private val MIN_NAME_LENGTH = 2 // min allowed by client (todo: configurable?)

class CharacterListManager(
        private val realmId: RealmId,
        private val characterDao: CharacterDao,
        private val realmDao: RealmDao,
        configManager: ConfigManager) {
    private val config = CharacterConfig(configManager)

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

        val normalisedName = normaliseName(name)

        validateName(normalisedName) // Is valid name?
        if (!config.isCreatingCharacterEnabled(race, characterClass)) throw CharacterCreationDisabled // Can create for race?
        if (config.maxCharsPerAccount() <= characterDao.getCharacterCount(userId)) throw TooManyCharacters // Too many chars?
        if (characterDao.getCharacter(normalisedName) != null) throw CharacterNameInUse(normalisedName) // Name taken?
        if (config.allowMultipleTeamsInAccount() || characterDao.listCharacters(userId).any { it.race.team != race.team })
            throw MultipleTeamsNotAllowed // PvP team violation

        // Create character
        try {
            return characterDao.createCharacter(GameCharacter(
                    userId = userId,
                    name = normalisedName,
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

        } finally {
            // Update the user character count
            realmDao.setUserCharacterCount(realmId, userId, characterDao.getCharacterCount(userId))
        }
    }

    fun deleteCharacter(userId: UserId, characterId: CharacterId) {
        // Get the character
        val character = getCharacter(characterId)
                ?.takeIf { it.userId == userId } // (if it belongs to current user)
                ?: return logger.warn { "Attempted to delete character $characterId: non-existent or not belonging to user $userId" }

        // TODO: nope out if current player
        // TODO: nope if guild leader
        // TODO: logout player if online
        try {
            characterDao.deleteCharacter(character)

        } finally {
            // Update the user character count
            realmDao.setUserCharacterCount(realmId, userId, characterDao.getCharacterCount(userId))
        }
    }

    fun getCharacters(userId: UserId) = characterDao.listCharacters(userId)
    private fun getCharacter(characterId: CharacterId) = characterDao.getCharacter(characterId)

    private fun normaliseName(name: String): String = name
            .toLowerCase()
            // TODO: do we need to de-utf8 or anything? Do we care?
            .capitalize()

    private fun validateName(name: String) {
        if (name.isBlank()) throw CharacterNameBlank(name)
        if (name.length < MIN_NAME_LENGTH) throw CharacterNameTooShort(name)
        if (name.length > MAX_NAME_LENGTH) throw CharacterNameTooLong(name)
        if (config.isNameReserved(name)) throw CharacterNameReserved(name)
        // TODO: support checking if name is mixed languages, if we even care
    }

    companion object: KLogging()

    /**
     * Exceptions related to character creation
     */
    sealed class CharacterCreationFailure(reason: String): Exception(reason) {
        object CharacterCreationDisabled: CharacterCreationFailure("Character creation is disabled")
        object TooManyCharacters: CharacterCreationFailure("Too many characters")
        object MultipleTeamsNotAllowed: CharacterCreationFailure("Multiple teams per account isn't allowed")
        sealed class IllegalCharacterName(name: String, reason: String): CharacterCreationFailure("Name $name was not valid ($reason)") {
            class CharacterNameTooShort(name: String) : IllegalCharacterName(name, "Length too short (<$MIN_NAME_LENGTH)")
            class CharacterNameTooLong(name: String) : IllegalCharacterName(name, "Length too short (>$MAX_NAME_LENGTH)")
            class CharacterNameBlank(name: String) : IllegalCharacterName(name, "Blank")
            class CharacterNameReserved(name: String) : IllegalCharacterName(name, "Reserved")
            class CharacterNameInUse(name: String) : IllegalCharacterName(name, "In use")
        }
    }

}