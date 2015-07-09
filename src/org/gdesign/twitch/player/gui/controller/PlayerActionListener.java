package org.gdesign.twitch.player.gui.controller;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.player.config.PlayerConfiguration;
import org.gdesign.twitch.player.gui.controller.provider.AuthorizedStreamProvider;
import org.gdesign.twitch.player.gui.controller.provider.ViewerStreamProvider;
import org.gdesign.twitch.player.gui.view.streams.StreamMenuItem;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory.MessageType;
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
				String token = TwitchAPI.authorizeApp(Permission.user_read,Permission.user_follows_edit);
				PlayerConfiguration cfg = ResourceManager.getPlayerConfiguration();
				cfg.token = token;
				ResourceManager.setPlayerConfiguration(cfg);
				controller.setChannelProvider(new AuthorizedStreamProvider(controller));
				controller.forceUpdate();
			} catch (InterruptedException | IOException | URISyntaxException | TwitchAPINoTokenSpecifiedException
					| TwitchAPINoPermissionException | TwitchAPIAuthTokenInvalidException e1) {
				LogManager.getLogger().error(e1);
				MessagePopupFactory.getFactory().createMessagePopup(MessageType.ERROR, e1.toString(), true);
			} 
		} else if (e.getActionCommand().contains("Left sided streamlist")) {
			controller.view.setStreamListPosition(BorderLayout.WEST);
		} else if (e.getActionCommand().contains("Right sided streamlist")) {
			controller.view.setStreamListPosition(BorderLayout.EAST);
		} else if (e.getActionCommand().contains("Show following streams")) {
			try {
				controller.setChannelProvider(new AuthorizedStreamProvider(controller));
			} catch (IOException | TwitchAPINoTokenSpecifiedException
					| TwitchAPINoPermissionException
					| TwitchAPIAuthTokenInvalidException e1) {
				LogManager.getLogger().error(e1);
				MessagePopupFactory.getFactory().createMessagePopup(MessageType.ERROR, e1.toString(), true);
			}
		} else if (e.getActionCommand().contains("Show featured streams")) {

		} else if (e.getActionCommand().contains("Show Top 25 streams")) {
			controller.setChannelProvider(new ViewerStreamProvider(controller));
		} else if (e.getActionCommand().contains("Show Top X streams")) {
			controller.setChannelProvider(new ViewerStreamProvider(controller,
					getValueFromOptionsPane("Streams sorted by views", "Please enter the amount of streams you want to see.", Integer.class)));
		} else if (e.getActionCommand().contains("Follow")) {
			StreamMenuItem p = (StreamMenuItem) e.getSource();
			try {
				TwitchAPI.followStream(p.getName());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getActionCommand().contains("Unfollow")) {
			StreamMenuItem p = (StreamMenuItem) e.getSource();
			try {
				TwitchAPI.unfollowStream(p.getName());
				controller.view.getStreamList().removeStreamView(p.getName());
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		} else if (e.getActionCommand().contains("Chat")) {
			StreamMenuItem p = (StreamMenuItem) e.getSource();
			try {
				Desktop.getDesktop().browse(new URI("http://twitch.tv/"+p.getName()+"/chat"));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValueFromOptionsPane(String title, String message, Class<T> clazz){
		if (clazz.equals(Integer.class)) {
			Integer data = Integer.valueOf(JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE));
			return clazz.cast(data);
		} else {
			T data = (T) JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
			return data;
		}
	}

}
