package com.thenorsepantheon.groupironman;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GroupIronManPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GroupIronManPlugin.class);
		RuneLite.main(args);
	}
}