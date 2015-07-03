package org.gdesign.twitch.player.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.exception.TwitchAPIUnauthorizedAccessException;
import org.gdesign.twitch.api.resource.users.User;
import org.gdesign.twitch.player.gui.view.MenuBar.CustomMenuItem;
import org.gdesign.twitch.player.gui.view.layout.channel.strategy.ChannelLayoutNormal;
import org.gdesign.twitch.player.gui.view.layout.channel.strategy.ChannelLayoutSmall;

public class PlayerActionListener implements ActionListener {

	private MainController controller;
	
	public PlayerActionListener(MainController controller) {
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("Account")){
			String eingabe = JOptionPane.showInputDialog(null,"Please enter your twitch.tv accountname:","Change user", JOptionPane.PLAIN_MESSAGE);
			try {
				User user = new TwitchAPI().getResource("https://api.twitch.tv/kraken/users/"+eingabe, User.class);
				if (user.display_name.length() != 0) {
					controller.model.getChannelListModel().removeAll();
					controller.view.getChannelListView().sortChannels(null);
					controller.setUsername(eingabe);	
					controller.updateImmediatly();
				}
			} catch (IOException | TwitchAPIUnauthorizedAccessException e1) {
				LogManager.getLogger().warn("User with name "+eingabe+" is not valid.\n"+e1);
			}

		} else if (e.getActionCommand().contains("Small channels")) {
			controller.view.getChannelListView().setChannelLayoutStrategy(new ChannelLayoutSmall());
		} else if (e.getActionCommand().contains("Normal channels")) {
			controller.view.getChannelListView().setChannelLayoutStrategy(new ChannelLayoutNormal());
		} else if (e.getActionCommand().contains("Hide offline streams")) {
			((CustomMenuItem) e.getSource()).setActionCommand("Show offline streams" );
			((CustomMenuItem) e.getSource()).setText("Show offline streams" );
			controller.view.getChannelListView().showOfflineStreams(false);
		} else if (e.getActionCommand().contains("Show offline streams")) {
			((CustomMenuItem) e.getSource()).setActionCommand("Hide offline streams" );
			((CustomMenuItem) e.getSource()).setText("Hide offline streams" );
			controller.view.getChannelListView().showOfflineStreams(true);
		}
	}

}
