package com.profiles.Profiles;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("profiles")
public interface ProfilesConfig extends Config
{

	@ConfigItem(
			keyName = "profilesData",
			name = "",
			description = "",
			hidden = true
	)
	default String profilesData()
	{
		return "";
	}

	@ConfigItem(
			keyName = "profilesData",
			name = "",
			description = ""
	)
	void profilesData(String str);

	@ConfigItem(
			keyName = "streamerMode",
			name = "Streamer Mode",
			description = "Hides your account emails"
	)
	default boolean isStreamerMode()
	{
		return false;
	}

}