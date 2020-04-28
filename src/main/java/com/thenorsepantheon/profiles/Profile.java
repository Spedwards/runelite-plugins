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

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Profile
{
	@Getter
	private static final List<Profile> profiles = new ArrayList<>();

	@Getter
	@Setter
	private static String encryptionPassword = null;

	private final String label;
	private final String login;
	@Setter
	private String password;
	@Setter
	private boolean encrypted;

	public Profile(String label, String login, String password)
	{
		this(label, login, password, true);
	}

	public Profile(String label, String login, String password, boolean encrypted)
	{
		this.label = label;
		this.login = login;
		this.password = password;
		this.encrypted = encrypted;
		profiles.add(this);
	}

	protected void encrypt()
	{
		if (this.getPassword() != null && !this.isEncrypted() && getEncryptionPassword() != null)
		{
			this.setPassword(AES.encrypt(this.getPassword(), getEncryptionPassword()));
			this.setEncrypted(true);
		}
	}

	protected void decrypt()
	{
		if (getEncryptionPassword() != null)
		{
			this.setPassword(AES.decrypt(this.getPassword(), getEncryptionPassword()));
			this.setEncrypted(false);
		}
	}

	static void decryptPasswords()
	{
		profiles.stream().filter(profile ->
			profile.getPassword() != null && profile.isEncrypted()
		).forEach(Profile::decrypt);
	}
}
