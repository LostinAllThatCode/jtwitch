package org.gdesign.twitch.player.gui.controller;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JMenuItem;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.view.ChannelView;
import org.gdesign.twitch.player.gui.view.EmbeddedPlayerControlView;
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
		if (e.getComponent().isEnabled()){
			if (e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = controller.model.getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()){
					try {
						LivestreamerInstance i = LivestreamerFactory.startInstance("twitch.tv/"+channel.getName(), LivestreamerFactory.getDefaultQuality());
						i.addListener(controller);
						((ChannelView)e.getComponent()).setHover(false);
					} catch (LivestreamerAlreadyRunningException  e1) {
						LogManager.getLogger().error(e1);
					} 
				}
			} else if (e.getComponent().getClass().equals(EmbeddedPlayerControlView.class)){
				switch (((EmbeddedPlayerControlView) e.getComponent()).getControlType()) {
					case PLAY_STOP:
						if (controller.view.getEmbeddedPlayerView().isPlaying())  controller.model.getEmbeddedPlayerModel().getInstance().stopStream();
						break;
					case FULLSCREEN:
						controller.view.getEmbeddedPlayerView().toggleFullscreen();
						if (controller.view.getEmbeddedPlayerView().isFullscreen()) controller.view.getChannelListView().setVisible(false);
						else controller.view.getChannelListView().setVisible(true);
						break;
					case VOLUME:
						controller.view.getEmbeddedPlayerView().setVolume(e.getX());
						break;
					case QUALITY:
						controller.view.getEmbeddedPlayerView().toggleQualityPopup();
					default:
						break;
				}
			} else if (e.getComponent().getClass().equals(JMenuItem.class)){
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
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
			ChannelModel channel = controller.model.getChannelListModel().getChannel(e.getComponent().getName());
			if (channel.isOnline()) {
				((ChannelView) e.getComponent()).setHover(true);
			}
		} else if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(EmbeddedPlayerControlView.class)){
			((EmbeddedPlayerControlView) e.getComponent()).setHover(true);
		} else if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(JMenuItem.class)){
			e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
			ChannelModel channel = controller.model.getChannelListModel().getChannel(e.getComponent().getName());
			if (channel.isOnline()) {
				((ChannelView) e.getComponent()).setHover(false);
			}		
		} else if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(EmbeddedPlayerControlView.class)){
			((EmbeddedPlayerControlView) e.getComponent()).setHover(false);
		} else if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(JMenuItem.class)){
			e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) controller.view.getEmbeddedPlayerView().setVolume(controller.view.getEmbeddedPlayerView().getVolume() + 10); 
		else controller.view.getEmbeddedPlayerView().setVolume(controller.view.getEmbeddedPlayerView().getVolume() - 10);
	}
	
}
