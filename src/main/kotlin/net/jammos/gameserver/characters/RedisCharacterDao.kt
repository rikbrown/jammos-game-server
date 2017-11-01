package net.jammos.gameserver.characters

import com.lambdaworks.redis.RedisClient
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import net.jammos.utils.auth.UserId
import net.jammos.utils.json.fromJson
import net.jammos.utils.json.toJson

class RedisCharacterDao(redisClient: RedisClient): CharacterDao {
    private val conn = redisClient.connect().sync()

    override fun listCharacters(userId: UserId): Set<GameCharacter> {
        return runBlocking {
            conn.smembers(userCharactersKey(userId))
                    .map { CharacterId(it.toLong()) }
                    .map { async { getCharacter(it) } }
                    .mapNotNull { it.await() }
                    .toSet()
        }
    }

    override fun createCharacter(userId: UserId, character: GameCharacter) {
        // Create character
        conn.set(characterKey(character.id), character.toJson())

        // Append to user characters
        conn.sadd(userCharactersKey(userId), character.id.toString())

        // Fix last character ID just in case
        if (conn.get(lastCharacterIdKey)?.toLong()?:0 < character.id.characterId) {
            conn.set(lastCharacterIdKey, character.id.characterId.toString())
        }
    }

    override fun nextCharacterId() = CharacterId(conn.incr(lastCharacterIdKey))

    private fun getCharacter(characterId: CharacterId): GameCharacter? {
        return conn.get(characterKey(characterId))?.fromJson()
    }
}

private fun userCharactersKey(userId: UserId) = "user:$userId:characters"
private fun characterKey(characterId: CharacterId) = "character:$characterId"
private val lastCharacterIdKey = "character:_lastId"

/*
    user:$userId:characters = set [$characterId, ...]
    character:$characterId = json {
        name: ...,
        ...
    }
    //character:_allIds = set [$characterId, ...]
    character_lastId = Long
 */