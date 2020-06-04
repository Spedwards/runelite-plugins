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
	// loadProfiles is getting called twice from plugin startUp. This ensures it only gets called once.
	private static boolean hasLoaded = false;

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
		List<Profile> profiles = Profile.getProfiles();
		profiles.forEach(Profile::encrypt);
		gson.toJson(profiles, writer);
		writer.flush();
		writer.close();
		profiles.forEach(Profile::decrypt);
	}

	static void loadProfiles() throws IOException
	{
		if (hasLoaded) return;
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
		profiles.forEach(p -> new Profile(p.getLabel(), p.getLogin(), p.getWorld(), p.getPassword()));

		hasLoaded = true;
	}
}
