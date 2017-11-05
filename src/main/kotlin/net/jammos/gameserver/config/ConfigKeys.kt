package net.jammos.gameserver.config

import net.jammos.gameserver.characters.Team
import net.jammos.utils.json.objectMapper
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object ConfigKeys {
    val IS_RACE_CHARACTER_CREATION_ENABLED = Team.values().associateBy({ it }, { BooleanConfigKey("IsRaceCharacterCreationEnabled.${it.name}", true) })
    val RESERVED_CHARACTER_NAMES = SetConfigKey("ReservedCharacterNames")
    val MAX_CHARS_PER_ACCOUNT = IntConfigKey("MaxCharsPerAccount", 10)
    val ALLOW_MULTIPLE_TEAMS_IN_ACCOUNT = BooleanConfigKey("AllowMultipleTeamsInAccount", false)

    val values: Set<ConfigKey<*>> = ConfigKeys::class.memberProperties
            .map { it.get(this) }
            .flatMap { (it as? Map<*, *>)?.values ?: setOf(it) } // if it's a map property, get it's values
            .filter { it is ConfigKey<*> }
            .map { it as ConfigKey<*> }
            .toSet()
}

sealed class ConfigKey<T>(private val fromString: (String) -> T, private val toString: (T) -> String = { it.toString() }) {
    abstract val key: String
    abstract val default: T?
    fun valueToString(value: T) = toString(value)
    fun valueFromString(string: String) = fromString(string)

    override fun toString() = key
}

data class ConfigEntry<T>(private val configKey: ConfigKey<T>, private val value: T) {
    val key = configKey.key
    val valueString by lazy { configKey.valueToString(value) }
}

class StringConfigKey(override val key: String, override val default: String? = null): ConfigKey<String>({ it }, { it })
class BooleanConfigKey(override val key: String, override val default: Boolean = false): ConfigKey<Boolean>(String::toBoolean)
@Suppress("UNCHECKED_CAST") class SetConfigKey(override val key: String, override val default: Set<String> = setOf()): JsonConfigKey<Set<String>>(key, Set::class as KClass<Set<String>>, setOf())
class IntConfigKey(override val key: String, override val default: Int = 0): ConfigKey<Int>(String::toInt)
open class JsonConfigKey<T: Any>(override val key: String, clazz: KClass<T>, override val default: T? = null): ConfigKey<T>({ objectMapper.readValue(it, clazz.java) }, objectMapper::writeValueAsString)

