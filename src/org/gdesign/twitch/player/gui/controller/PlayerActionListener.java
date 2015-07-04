package org.gdesign.twitch.player.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.gui.controller.provider.AuthorizedStreamProvider;
import org.gdesign.twitch.player.gui.view.MenuBar.CustomMenuItem;
import org.gdesign.twitch.player.gui.view.layout.channel.strategy.ChannelLayoutNormal;
import org.gdesign.twitch.player.gui.view.layout.channel.strategy.ChannelLayoutSmall;
import org.gdesign.utils.ResourceManager;
public class PlayerActionListener implements ActionListener {

	private MainController controller;
	
	public PlayerActionListener(MainController controller) {
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("Authorize")){
			try {
				TwitchAPI.authorizeApp(Permission.user_read);
				String eingabe = JOptionPane.showInputDialog(null,"Please copy paste your access token here",
						 "Authorize app",
                        JOptionPane.PLAIN_MESSAGE);
                  				
				if (eingabe.trim().length() == 0) { 
					throw new IOException("Token entered is wrong");
				} else {
					PlayerConfiguration cfg = ResourceManager.getPlayerConfiguration();
					cfg.token = eingabe.trim();
					ResourceManager.setPlayerConfiguration(cfg);
					controller.setChannelProvider(new AuthorizedStreamProvider(controller));
					controller.updateImmediatly();
				}

			} catch (IOException | URISyntaxException | TwitchAPINoTokenSpecifiedException
					| TwitchAPINoPermissionException | TwitchAPIAuthTokenInvalidException e1) {
				e1.printStackTrace();
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
