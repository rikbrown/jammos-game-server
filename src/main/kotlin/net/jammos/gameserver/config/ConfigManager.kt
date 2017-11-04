package net.jammos.gameserver.config

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import mu.KLogging
import java.util.*

class ConfigManager(val configDao: ConfigDao) {
    // Loading cache to store configuration values
    // LoadingCache doesn't support null so we'll use Java optional here
    private var config: LoadingCache<ConfigKey<*>, Optional<Any>> = CacheBuilder.newBuilder()
            //.maximumSize(1000)
            //.expireAfterWrite(10, TimeUnit.MINUTES)
            .build(object : CacheLoader<ConfigKey<*>, Optional<Any>>() {
                override fun load(key: ConfigKey<*>): Optional<Any> {
                    logger.info { "Loading config key: $key" }
                    return Optional.ofNullable(configDao.get(key))
                }
            })

    init {
        // Preload all known config keys
        logger.info { "Preloading all config keys..." }
        config.getAll(ConfigKeys.values().map { it.key })
        logger.info { "Preloaded all config keys!" }
    }

    @Suppress("UNCHECKED_CAST") // guaranteed by loading cache
    fun <T> get(key: ConfigKey<T>): T? = config.get(key).orElse(null) as T? ?: key.default

    fun <T> set(key: ConfigKey<T>, value: T) = {
        configDao.set(ConfigEntry(key, value))
        config.refresh(key)
    }

    companion object: KLogging()
}