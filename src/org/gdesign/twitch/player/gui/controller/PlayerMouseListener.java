package org.gdesign.twitch.player.gui.controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

import javax.swing.JMenuItem;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.TwitchAPI.Permission;
import org.gdesign.twitch.api.exception.TwitchAPIAuthTokenInvalidException;
import org.gdesign.twitch.api.exception.TwitchAPINoPermissionException;
import org.gdesign.twitch.api.exception.TwitchAPINoTokenSpecifiedException;
import org.gdesign.twitch.player.gui.view.embeddedplayer.EmbeddedPlayerControlView;
import org.gdesign.twitch.player.gui.view.streams.StreamMenu;
import org.gdesign.twitch.player.gui.view.streams.StreamPopup;
import org.gdesign.twitch.player.gui.view.streams.StreamView;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory.MessageType;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.twitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.twitch.player.livestreamer.exception.LivestreamerAlreadyRunningException;

public class PlayerMouseListener extends MouseAdapter {

	private MainController controller;

	public PlayerMouseListener(MainController controller) {
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getComponent().isEnabled()) {
			Class<?> clazz = e.getComponent().getClass();
			if (clazz.equals(StreamView.class)) {
				if (e.getButton() == 1) { // Left clicked stream
					try {
						String channel = e.getComponent().getName();
						LivestreamerFactory.startInstance("twitch.tv/" + channel,LivestreamerFactory.getDefaultQuality());
					} catch (LivestreamerAlreadyRunningException e1) {
						LogManager.getLogger().error(e1);
						MessagePopupFactory.getFactory().createMessagePopup(MessageType.WARN, e1.toString(), true);
					}					
				} else if (e.getButton() == 3) { // Right clicked stream
					try {
						if (TwitchAPI.hasPermisson(Permission.user_follows_edit))
							new StreamPopup((StreamView) e.getComponent(),e, controller.actionListener);
					} catch (IOException | TwitchAPINoPermissionException | TwitchAPINoTokenSpecifiedException | TwitchAPIAuthTokenInvalidException e1) {
						e1.printStackTrace();
						MessagePopupFactory.getFactory().createMessagePopup(MessageType.WARN, e1.toString(), true);
					}				
				}

			} else if (clazz.equals(EmbeddedPlayerControlView.class)) {
				switch (((EmbeddedPlayerControlView) e.getComponent()).getControlType()) {
					case PLAY_STOP:
						if (controller.view.getEmbeddedPlayer().isPlaying())
							controller.model.getEmbeddedPlayerModel().getInstance().stopStream();
						break;
					case FULLSCREEN:
						controller.view.getEmbeddedPlayer().toggleFullscreen();
						if (controller.view.getEmbeddedPlayer().isFullscreen()) {
							controller.view.getStreamList().setVisible(false);
							controller.view.getStreamList().getStreamMenuBar().setVisible(false);
						} else {
							controller.view.getStreamList().setVisible(true);
							controller.view.getStreamList().getStreamMenuBar().setVisible(true);
						}
						break;
					case VOLUME:
						controller.view.getEmbeddedPlayer().setVolume(e.getX());
						break;
					case QUALITY:
						controller.view.getEmbeddedPlayer()
								.toggleQualityPopup();
					default:
						break;
				}
			} else if (clazz.equals(JMenuItem.class)) {
				String quality = ((JMenuItem) e.getComponent()).getText().toLowerCase();
				if (LivestreamerFactory.getDefaultQuality().compareTo(quality) != 0) {
					LivestreamerFactory.setDefaultQuality(quality);
					LivestreamerInstance instance = controller.model.getEmbeddedPlayerModel().getInstance();
					if (instance != null) {
						try {
							LivestreamerFactory.stopInstance(instance);
							LivestreamerInstance newInstance = LivestreamerFactory.startInstance(instance.getStream(), LivestreamerFactory.getDefaultQuality());
							newInstance.addListener(controller);
						} catch (LivestreamerAlreadyRunningException e1) {
							LogManager.getLogger().error(e1);
						}
					}
				}
				controller.view.getEmbeddedPlayer().toggleQualityPopup();
			} else if (clazz.equals(StreamMenu.class)) {
				LogManager.getLogger().debug(e.getComponent().getName());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent().isEnabled()) {
			Class<?> clazz = e.getComponent().getClass();
			if (clazz.equals(StreamView.class)) {
				((StreamView) e.getComponent()).setHover(true);
			} else if (clazz.equals(EmbeddedPlayerControlView.class)) {
				((EmbeddedPlayerControlView) e.getComponent()).setHover(true);
			} else if (clazz.equals(JMenuItem.class)) {
				JMenuItem m = (JMenuItem) e.getComponent();
				if (m.getText().toLowerCase().compareTo(LivestreamerFactory.getDefaultQuality().toLowerCase()) != 0) {
					e.getComponent().setBackground(new Color(80, 40, 180));
					e.getComponent().setForeground(Color.WHITE);
				}
			}
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getComponent().isEnabled()) {
			Class<?> clazz = e.getComponent().getClass();
			if (clazz.equals(StreamView.class)) {
				((StreamView) e.getComponent()).setHover(false);
			} else if (clazz.equals(EmbeddedPlayerControlView.class)) {
				((EmbeddedPlayerControlView) e.getComponent()).setHover(false);
			} else if (clazz.equals(JMenuItem.class)) {
				JMenuItem m = (JMenuItem) e.getComponent();
				if (m.getText().toLowerCase().compareTo(LivestreamerFactory.getDefaultQuality().toLowerCase()) != 0) {
					e.getComponent().setBackground(Color.DARK_GRAY.darker());
					e.getComponent().setForeground(Color.WHITE.darker());
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getComponent().isEnabled()) {
			Class<?> clazz = e.getComponent().getClass();
			if (clazz.equals(EmbeddedPlayerControlView.class)) {
				((EmbeddedPlayerControlView) e.getComponent()).setHover(true);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getComponent().isEnabled()&& e.getComponent().getClass().equals(EmbeddedPlayerControlView.class)) {
			switch (((EmbeddedPlayerControlView) e.getComponent()).getControlType()) {
				case VOLUME:
					((EmbeddedPlayerControlView) e.getComponent()).setHover(true);
					controller.view.getEmbeddedPlayer().setVolume(e.getX());
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) controller.view.getEmbeddedPlayer().setVolume(controller.view.getEmbeddedPlayer().getVolume() + 10);
		else controller.view.getEmbeddedPlayer().setVolume(controller.view.getEmbeddedPlayer().getVolume() - 10);
	}

}
