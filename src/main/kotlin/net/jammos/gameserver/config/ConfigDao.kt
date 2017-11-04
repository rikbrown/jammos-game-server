package net.jammos.gameserver.config

import net.jammos.utils.json.objectMapper
import kotlin.reflect.KClass

interface ConfigDao {
    fun <T> set(entry: ConfigEntry<T>)
    fun <T> get(key: ConfigKey<T>): T?
}

sealed class ConfigKey<T>(private val fromString: (String) -> T, private val toString: (T) -> String = { it.toString() }) {
    abstract val key: String
    abstract val default: T?
    fun valueToString(value: T) = toString(value)
    fun valueFromString(string: String) = fromString(string)

    override fun toString() = key
}

class StringConfigKey(override val key: String, override val default: String? = null): ConfigKey<String>({ it }, { it })
class BooleanConfigKey(override val key: String, override val default: Boolean = false): ConfigKey<Boolean>(String::toBoolean)
class JsonConfigKey<T: Any>(override val key: String, clazz: KClass<T>, override val default: T? = null): ConfigKey<T>({ objectMapper.readValue(it, clazz.java) }, objectMapper::writeValueAsString)

data class ConfigEntry<T>(private val configKey: ConfigKey<T>, private val value: T) {
    val key = configKey.key
    val valueString by lazy { configKey.valueToString(value) }
}