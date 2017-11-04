package net.jammos.gameserver.config

import com.lambdaworks.redis.RedisClient

const private val configRedisKey = "config"
class RedisConfigDao(redisClient: RedisClient): ConfigDao {
    private val conn = redisClient.connect().sync()

    override fun <T> set(entry: ConfigEntry<T>) {
        conn.hset(configRedisKey, entry.key, entry.valueString)
    }

    override fun <T> get(key: ConfigKey<T>): T? =
        conn.hget(configRedisKey, key.key)?.let { key.valueFromString(it) }

}