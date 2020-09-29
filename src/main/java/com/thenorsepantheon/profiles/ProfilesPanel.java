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

import com.thenorsepantheon.profiles.ui.Button;
import com.thenorsepantheon.profiles.ui.PasswordField;
import com.thenorsepantheon.profiles.ui.TextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

@Slf4j
class ProfilesPanel extends PluginPanel
{

	private static final String ACCOUNT_USERNAME = "Account Username";
	private static final String ACCOUNT_WORLD = "Account World";
	private static final String ACCOUNT_LABEL = "Account Label";

	private final Client client;
	private static ProfilesConfig profilesConfig;

	private final TextField txtAccountLabel = new TextField(ACCOUNT_LABEL);
	private final PasswordField txtAccountLogin;
	private final PasswordField txtAccountWorld;
	private final JPanel profilesPanel = new JPanel();
	private final GridBagConstraints c;

	@Inject
	public ProfilesPanel(Client client, ProfilesConfig config)
	{
		super();
		this.client = client;
		profilesConfig = config;

		setBorder(new EmptyBorder(18, 10, 0, 10));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new GridBagLayout());

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0, 0, 3, 0);

		add(txtAccountLabel, c);
		c.gridy++;

		txtAccountLogin = new PasswordField(ACCOUNT_USERNAME, profilesConfig.isStreamerMode());
		txtAccountWorld = new PasswordField(ACCOUNT_WORLD, profilesConfig.isStreamerMode());

		add(txtAccountLogin, c);
		c.gridy++;

		add(txtAccountWorld, c);
		c.gridy++;

		c.insets = new Insets(0, 0, 15, 0);

		Button btnAddAccount = new Button("Add Account");
		btnAddAccount.addActionListener(e ->
		{
			String labelText = txtAccountLabel.getText();
			String loginText = String.valueOf(txtAccountLogin.getPassword());
			String worldText = String.valueOf(txtAccountWorld.getPassword());
			if (labelText.equals(ACCOUNT_LABEL) || loginText.equals(ACCOUNT_USERNAME))
			{
				return;
			}
			if (!worldText.matches("\\d+") && !worldText.equals(ACCOUNT_WORLD)) {
				JOptionPane.showMessageDialog(ProfilesPanel.this,
					"The world entered is not an integer.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			Integer world = worldText.equals(ACCOUNT_WORLD) ? null : Integer.parseInt(worldText);
			Profile profile = new Profile(labelText, loginText, world);
			this.addProfile(profile);
			try
			{
				ProfilesStorage.saveProfiles();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}

			txtAccountLabel.resetState();
			txtAccountLogin.resetState();
			txtAccountWorld.resetState();
		});

		txtAccountLogin.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					btnAddAccount.doClick();
					btnAddAccount.requestFocus();
				}
			}
		});

		add(btnAddAccount, c);
		c.gridy++;

		profilesPanel.setLayout(new GridBagLayout());
		add(profilesPanel, c);
		c.gridy = 0;
		c.insets = new Insets(0, 0, 5, 0);

		Profile.getProfiles().forEach(this::addProfile);
	}

	void redrawProfiles()
	{
		txtAccountLogin.setObfuscate(profilesConfig.isStreamerMode());
		txtAccountWorld.setObfuscate(profilesConfig.isStreamerMode());
		profilesPanel.removeAll();
		c.gridy = 0;
		Profile.getProfiles().forEach(this::addProfile);
	}

	private void addProfile(Profile profile)
	{
		ProfilePanel profilePanel = new ProfilePanel(client, profile, profilesConfig, this);
		c.gridy++;
		profilesPanel.add(profilePanel, c);

		revalidate();
		repaint();
	}
}