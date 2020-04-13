package com.thenorsepantheon.profiles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

@Slf4j
class ProfilePanel extends JPanel
{
	private static final ImageIcon DELETE_ICON;
	private static final ImageIcon DELETE_HOVER_ICON;

	static
	{
		final BufferedImage deleteImg = ImageUtil.getResourceStreamFromClass(ProfilesPlugin.class, "delete_icon.png");
		DELETE_ICON = new ImageIcon(deleteImg);
		DELETE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(deleteImg, -100));
	}

	private final String loginText;

	ProfilePanel(final Client client, Profile profile, ProfilesConfig config)
	{
		this.loginText = profile.getLogin();

		final ProfilePanel panel = this;

		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JPanel labelWrapper = new JPanel(new BorderLayout());
		labelWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		labelWrapper.setBorder(new CompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR),
			BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR)
		));

		JPanel panelActions = new JPanel(new BorderLayout(3, 0));
		panelActions.setBorder(new EmptyBorder(0, 0, 0, 8));
		panelActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JLabel delete = new JLabel();
		delete.setIcon(DELETE_ICON);
		delete.setToolTipText("Delete account profile");
		delete.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				panel.getParent().remove(panel);
				Profile.getProfiles().removeIf(p -> p.getLabel().equals(profile.getLabel())
					&& p.getLogin().equals(profile.getLogin()));
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				delete.setIcon(DELETE_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				delete.setIcon(DELETE_ICON);
			}
		});

		panelActions.add(delete, BorderLayout.EAST);

		JLabel label = new JLabel();
		label.setText(profile.getLabel());
		label.setBorder(null);
		label.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		label.setPreferredSize(new Dimension(0, 24));
		label.setForeground(Color.WHITE);
		label.setBorder(new EmptyBorder(0, 8, 0, 0));

		labelWrapper.add(label, BorderLayout.CENTER);
		labelWrapper.add(panelActions, BorderLayout.EAST);
		label.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e) && client.getGameState() == GameState.LOGIN_SCREEN)
				{
					client.setUsername(loginText);
				}
			}
		});

		JPanel bottomContainer = new JPanel(new BorderLayout());
		bottomContainer.setBorder(new EmptyBorder(8, 0, 8, 0));
		bottomContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		bottomContainer.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e) && client.getGameState() == GameState.LOGIN_SCREEN)
				{
					client.setUsername(loginText);
				}
			}
		});

		JLabel login = new JLabel();
		login.setText(config.isStreamerMode() ? "Hidden email" : loginText);
		login.setBorder(null);
		login.setPreferredSize(new Dimension(0, 24));
		login.setForeground(Color.WHITE);
		login.setBorder(new EmptyBorder(0, 8, 0, 0));

		bottomContainer.add(login, BorderLayout.CENTER);

		add(labelWrapper, BorderLayout.NORTH);
		add(bottomContainer, BorderLayout.CENTER);
	}
}