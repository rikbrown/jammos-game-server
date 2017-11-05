package net.jammos.gameserver.characters

import net.jammos.gameserver.config.ConfigKeys.ALLOW_MULTIPLE_TEAMS_IN_ACCOUNT
import net.jammos.gameserver.config.ConfigKeys.IS_RACE_CHARACTER_CREATION_ENABLED
import net.jammos.gameserver.config.ConfigKeys.MAX_CHARS_PER_ACCOUNT
import net.jammos.gameserver.config.ConfigKeys.RESERVED_CHARACTER_NAMES
import net.jammos.gameserver.config.ConfigManager

class CharacterConfig(private val config: ConfigManager) {

    fun isCreatingCharacterEnabled(race: Race, characterClass: CharacterClass) =
            // TODO: support class?
            IS_RACE_CHARACTER_CREATION_ENABLED[race.team]?.let { config.get(it) } == true

    fun isNameReserved(name: String) = config.get(RESERVED_CHARACTER_NAMES).contains(name)
    fun maxCharsPerAccount() = config.get(MAX_CHARS_PER_ACCOUNT)
    fun allowMultipleTeamsInAccount() = config.get(ALLOW_MULTIPLE_TEAMS_IN_ACCOUNT)

}