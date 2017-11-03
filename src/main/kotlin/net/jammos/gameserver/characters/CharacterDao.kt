package net.jammos.gameserver.characters

import net.jammos.utils.auth.UserId

interface CharacterDao {
    fun createCharacter(character: GameCharacter): GameCharacter
    fun listCharacters(userId: UserId): Set<GameCharacter>
    fun getCharacter(characterId: CharacterId): GameCharacter?
    fun deleteCharacter(character: GameCharacter)
}