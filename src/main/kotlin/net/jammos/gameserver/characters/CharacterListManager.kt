package net.jammos.gameserver.characters

import net.jammos.utils.auth.UserId

class CharacterListManager(private val characterDao: CharacterDao) {

    fun getCharacters(userId: UserId): Set<GameCharacter> {
        return characterDao.listCharacters(userId)
    }

}