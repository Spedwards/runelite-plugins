/*
 * Copyright (c) 2020, Spedwards <https://github.com/Spedwards>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thenorsepantheon.profiles;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.World;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import java.awt.image.BufferedImage;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.WorldResult;

@Slf4j
@PluginDescriptor(
	name = "Profiles"
)
public class ProfilesPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private WorldService worldService;

	@Inject
	private ProfilesConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	private ProfilesPanel panel;
	private NavigationButton navButton;

	private static ProfilesPlugin INSTANCE;

	@Provides
	ProfilesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProfilesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		INSTANCE = this;
		ProfilesStorage.loadProfiles();

		panel = new ProfilesPanel(client, config);

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "profiles_icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Profiles")
			.priority(8)
			.icon(icon)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("profiles"))
		{
			panel.redrawProfiles();
		}
	}

	protected static World findWorld(Client client, Integer worldInt)
	{
		if (worldInt == null)
		{
			return null;
		}

		final WorldResult worldResult = ProfilesPlugin.INSTANCE.worldService.getWorlds();
		if (worldResult == null)
		{
			log.warn("Failed to lookup worlds.");
			return null;
		}
		final net.runelite.http.api.worlds.World world = worldResult.findWorld(worldInt);

		if (world != null)
		{
			final World rsWorld = client.createWorld();
			rsWorld.setActivity(world.getActivity());
			rsWorld.setAddress(world.getAddress());
			rsWorld.setId(world.getId());
			rsWorld.setPlayerCount(world.getPlayers());
			rsWorld.setLocation(world.getLocation());
			rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));

			return rsWorld;
		}
		else
		{
			log.warn("World {} not found.", worldInt);
			return null;
		}
	}

}
