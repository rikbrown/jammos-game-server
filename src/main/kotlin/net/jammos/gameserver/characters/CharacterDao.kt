package net.jammos.gameserver.characters

import net.jammos.utils.auth.UserId

interface CharacterDao {
    fun listCharacters(userId: UserId): Set<GameCharacter>
    fun createCharacter(userId: UserId, character: GameCharacter)
    fun nextCharacterId(): CharacterId
}