package com.thenorsepantheon.profiles;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.runelite.client.RuneLite;

public class ProfilesStorage
{
	private static final File PROFILES;

	static
	{
		PROFILES = new File(RuneLite.RUNELITE_DIR, "profiles");
		PROFILES.mkdirs();
	}

	static void saveProfiles() throws IOException
	{
		File file = new File(PROFILES, "profiles.json");

		Gson gson = new Gson();
		Writer writer = new FileWriter(file);
		gson.toJson(Profile.getProfiles(), writer);
		writer.flush();
		writer.close();
	}

	static void loadProfiles() throws IOException
	{
		File file = new File(PROFILES, "profiles.json");
		if (!file.exists())
		{
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), StandardCharsets.UTF_8)))
			{
				writer.write("[]");
			}
		}

		Gson gson = new Gson();
		List<Profile> profiles = gson.fromJson(new FileReader(file), new TypeToken<List<Profile>>(){}.getType());
		profiles.forEach(p -> new Profile(p.getLabel(), p.getLogin()));
	}
}
