package com.thenorsepantheon.profiles;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Profiles"
)
public class ProfilesPlugin extends Plugin
{
	
	@Inject
	private Client client;

	@Inject
	private ProfilesConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	private ProfilesPanel panel;
	private NavigationButton navButton;

	@Provides
	ProfilesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProfilesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{

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

}
