package net.jammos.gameserver.config

enum class ConfigKeys(val key: ConfigKey<*>) {
    IS_CHARACTER_CREATION_ENABLED(JsonConfigKey("isCharacterCreationEnabled", CharacterCreationEnabled::class, CharacterCreationEnabled(false, false)))
}

data class CharacterCreationEnabled(val alliance: Boolean, val horde: Boolean)