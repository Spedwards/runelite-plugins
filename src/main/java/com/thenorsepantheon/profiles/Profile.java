package com.thenorsepantheon.profiles;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Profile
{
	@Getter
	private static final List<Profile> profiles = new ArrayList<>();

	private String label;
	private String login;

	public Profile(String label, String login)
	{
		this.label = label;
		this.login = login;
		profiles.add(this);
	}
}
