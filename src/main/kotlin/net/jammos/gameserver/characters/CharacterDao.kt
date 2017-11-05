package net.jammos.gameserver.characters

import net.jammos.utils.auth.UserId

interface CharacterDao {
    fun createCharacter(character: GameCharacter, overwriteExisting: Boolean = false): GameCharacter
    fun listCharacters(userId: UserId): Set<GameCharacter>
    fun getCharacter(characterId: CharacterId): GameCharacter?
    fun getCharacter(name: String): GameCharacter?
    fun deleteCharacter(character: GameCharacter)
}