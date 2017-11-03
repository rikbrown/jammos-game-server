package net.jammos.gameserver.characters

import com.lambdaworks.redis.RedisClient
import com.lambdaworks.redis.RedisFuture
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.runBlocking
import net.jammos.gameserver.characters.CharacterId.Companion.UNDEFINED
import net.jammos.utils.auth.UserId
import net.jammos.utils.json.fromJson
import net.jammos.utils.json.toJson

class RedisCharacterDao(redisClient: RedisClient): CharacterDao {

    private val conn = redisClient.connect().async()

    override fun createCharacter(character: GameCharacter): GameCharacter {
        val savedCharacter = if (character.id == UNDEFINED) character.copy(id = nextCharacterId()) else character

        // Create character
        conn.set(characterKey(savedCharacter.id), savedCharacter.toJson())

        // Append to user characters and all characters
        conn.sadd(userCharactersKey(savedCharacter.userId), savedCharacter.id.toString())
        conn.sadd(allCharacterIdsKey, savedCharacter.id.toString())

        // Fix last character ID if it was specified and higher, just in case
        if (character.id != UNDEFINED && conn.get(lastCharacterIdKey).get()?.toLong()?:0 < savedCharacter.id.characterId) {
            conn.set(lastCharacterIdKey, savedCharacter.id.characterId.toString())
        }

        return savedCharacter
    }

    override fun listCharacters(userId: UserId) = runBlocking {
        conn.smembers(userCharactersKey(userId)).get()
                .map { CharacterId(it.toLong()) }
                .map { conn.get(characterKey(it)) }
                .mapNotNull { it.await() }
                .map { it.fromJson<GameCharacter>() }
                .toSet()
    }

    override fun getCharacter(characterId: CharacterId): GameCharacter? {
        return conn.get(characterKey(characterId)).get()?.fromJson()
    }

    override fun deleteCharacter(character: GameCharacter) = runBlocking<Unit> {
        // Remove character (block on this)
        conn.del(characterKey(character.id)).await()

        // Remove from user's characters and all characters
        val jobs = mutableListOf<RedisFuture<*>>()
        jobs += conn.srem(userCharactersKey(character.userId), character.id.toString())
        jobs += conn.srem(allCharacterIdsKey, character.id.toString())
        jobs.forEach { it.await() }
    }

    private fun nextCharacterId() = CharacterId(conn.incr(lastCharacterIdKey).get())
}

private fun userCharactersKey(userId: UserId) = "user:$userId:characters"
private fun characterKey(characterId: CharacterId) = "character:$characterId"
private val lastCharacterIdKey = "character:_lastId"
private val allCharacterIdsKey = "character:_allIds"

/*
    user:$userId:characters = set [$characterId, ...]
    character:$characterId = json {
        name: ...,
        ...
    }
    //character:_allIds = set [$characterId, ...]
    character_lastId = Long
 */