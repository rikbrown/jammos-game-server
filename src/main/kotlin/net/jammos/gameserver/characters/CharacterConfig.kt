package net.jammos.gameserver.characters

import net.jammos.gameserver.config.ConfigKeys
import net.jammos.gameserver.config.ConfigManager

class CharacterConfig(private val config: ConfigManager) {

    fun isCreatingCharacterEnabled(race: Race, characterClass: CharacterClass) =
            // TODO: support class?
            ConfigKeys.IS_RACE_CHARACTER_CREATION_ENABLED[race.team]?.let { config.get(it) } == true

    fun isNameReserved(name: String) = config.get(ConfigKeys.RESERVED_CHARACTER_NAMES).contains(name)

}