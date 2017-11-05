package net.jammos.gameserver.config

interface ConfigDao {
    fun <T> set(entry: ConfigEntry<T>)
    fun <T> get(key: ConfigKey<T>): T?
}

