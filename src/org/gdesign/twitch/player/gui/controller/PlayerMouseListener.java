package org.gdesign.twitch.player.gui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JMenuItem;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.EmbeddedPlayerControlView;
import org.gdesign.twitch.player.gui.view.MenuBar;
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
			if (clazz.equals(ChannelView.class)) {
				ChannelModel channel = controller.model.getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()) {
					try {
						LivestreamerInstance i = LivestreamerFactory.startInstance("twitch.tv/" + channel.getName(),LivestreamerFactory.getDefaultQuality());
						i.addListener(controller);
						((ChannelView) e.getComponent()).setHover(false);
					} catch (LivestreamerAlreadyRunningException e1) {
						LogManager.getLogger().error(e1);
					}
				}
			} else if (clazz.equals(EmbeddedPlayerControlView.class)) {
				switch (((EmbeddedPlayerControlView) e.getComponent()).getControlType()) {
					case PLAY_STOP:
						if (controller.view.getEmbeddedPlayerView().isPlaying())
							controller.model.getEmbeddedPlayerModel().getInstance()
									.stopStream();
						break;
					case FULLSCREEN:
						controller.view.getEmbeddedPlayerView().toggleFullscreen();
						if (controller.view.getEmbeddedPlayerView().isFullscreen()) {
							controller.view.getChannelListView().setVisible(false);
							controller.view.getChannelListView().getMenuBar().setVisible(false);
						} else {
							controller.view.getChannelListView().setVisible(true);
							controller.view.getChannelListView().getMenuBar().setVisible(true);
						}
						break;
					case VOLUME:
						controller.view.getEmbeddedPlayerView().setVolume(e.getX());
						break;
					case QUALITY:
						controller.view.getEmbeddedPlayerView().setQuality(
								LivestreamerFactory.getDefaultQuality());
						controller.view.getEmbeddedPlayerView()
								.toggleQualityPopup();
					default:
						break;
				}
			} else if (clazz.equals(JMenuItem.class)) {
				String quality = ((JMenuItem) e.getComponent()).getText();
				LivestreamerFactory.setDefaultQuality(quality.toLowerCase());
				LivestreamerInstance instance = controller.model.getEmbeddedPlayerModel().getInstance();
				if (instance != null) {
					try {
						LivestreamerFactory.stopInstance(instance);
						LivestreamerInstance newInstance = LivestreamerFactory.startInstance(instance.getStream(), quality);
						newInstance.addListener(controller);
					} catch (LivestreamerAlreadyRunningException e1) {
						LogManager.getLogger().error(e1);
					}
				}
				controller.view.getEmbeddedPlayerView().toggleQualityPopup();
			} else if (clazz.equals(MenuBar.CustomMenuItem.class)) {
				LogManager.getLogger().debug(e.getComponent().getName());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent().isEnabled()) {
			Class<?> clazz = e.getComponent().getClass();
			if (clazz.equals(ChannelView.class)) {
				ChannelModel channel = controller.model.getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()) ((ChannelView) e.getComponent()).setHover(true);
			} else if (clazz.equals(EmbeddedPlayerControlView.class)) {
				((EmbeddedPlayerControlView) e.getComponent()).setHover(true);
			} else if (clazz.equals(JMenuItem.class)) {
				e.getComponent().setBackground(e.getComponent().getBackground().brighter());
			} 
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getComponent().isEnabled()) {
			Class<?> clazz = e.getComponent().getClass();
			if (clazz.equals(ChannelView.class)) {
				ChannelModel channel = controller.model.getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()) ((ChannelView) e.getComponent()).setHover(false);
			} else if (clazz.equals(EmbeddedPlayerControlView.class)) {
				((EmbeddedPlayerControlView) e.getComponent()).setHover(false);
			} else if (clazz.equals(JMenuItem.class)) {
				e.getComponent().setBackground(e.getComponent().getBackground().darker());
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
					controller.view.getEmbeddedPlayerView().setVolume(e.getX());
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) controller.view.getEmbeddedPlayerView().setVolume(controller.view.getEmbeddedPlayerView().getVolume() + 10);
		else controller.view.getEmbeddedPlayerView().setVolume(controller.view.getEmbeddedPlayerView().getVolume() - 10);
	}

}
