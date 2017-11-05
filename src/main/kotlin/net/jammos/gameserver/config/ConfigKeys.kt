package net.jammos.gameserver.config

import kotlin.reflect.full.memberProperties

object ConfigKeys {
    val IS_CHARACTER_CREATION_ENABLED = JsonConfigKey("isCharacterCreationEnabled", CharacterCreationEnabled::class, CharacterCreationEnabled(false, false))

    val values: Set<ConfigKey<*>> = ConfigKeys::class.memberProperties
            .map { it.get(this) }
            .filter { it is ConfigKey<*> }
            .map { it as ConfigKey<*> }
            .toSet()
}

data class CharacterCreationEnabled(val alliance: Boolean, val horde: Boolean)