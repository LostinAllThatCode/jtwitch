package org.gdesign.twitch.player.gui.listener;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JMenuItem;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.controller.MainController;
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
				ChannelModel channel = controller.getModel().getChannelListModel().getChannel(e.getComponent().getName());
				if (channel.isOnline()){
					try {
						LivestreamerInstance i = LivestreamerFactory.startInstance("twitch.tv/"+channel.getName(), LivestreamerFactory.getDefaultQuality());
						i.addListener(controller.getModel().getEmbeddedPlayerModel());
						controller.getView().getEmbeddedPlayerView().setControlValue("STATUS", "Starting livestreamer instance for "+i.getStream());
						((ChannelView) e.getComponent()).setHover(false);
					} catch (LivestreamerAlreadyRunningException  e1) {
						LogManager.getLogger().error(e1);
					} 
				}
			} else if (e.getComponent().getClass().equals(EmbeddedPlayerControlView.class)){
				switch (((EmbeddedPlayerControlView) e.getComponent()).getControlType()) {
					case PLAY_STOP:
						if (controller.getView().getEmbeddedPlayerView().isPlaying()) controller.getModel().getEmbeddedPlayerModel().getInstance().stopStream();
						break;
					case FULLSCREEN:
						controller.getView().getEmbeddedPlayerView().toggleFullscreen();
						if (controller.getView().getEmbeddedPlayerView().isFullscreen()) controller.getView().getChannelListView().setVisible(false);
						else controller.getView().getChannelListView().setVisible(true);
						break;
					case VOLUME:
						controller.getView().getEmbeddedPlayerView().setVolume(e.getX());
						break;
					case QUALITY:
						controller.getView().getEmbeddedPlayerView().toggleQualityPopup();
					default:
						break;
				}
			} else if (e.getComponent().getClass().equals(JMenuItem.class)){
				String quality = ((JMenuItem) e.getComponent()).getText();
				LivestreamerFactory.setDefaultQuality(quality.toLowerCase());
				LivestreamerInstance instance = controller.getModel().getEmbeddedPlayerModel().getInstance();
				if (instance != null) {
					try {
						LivestreamerFactory.stopInstance(instance);
						LivestreamerInstance newInstance = LivestreamerFactory.startInstance(instance.getStream(), quality);
						newInstance.addListener(controller.getModel().getEmbeddedPlayerModel());
					} catch (LivestreamerAlreadyRunningException e1) {
						LogManager.getLogger().error(e1);
					}
				}
				controller.getView().getEmbeddedPlayerView().toggleQualityPopup();
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
			ChannelModel channel = controller.getModel().getChannelListModel().getChannel(e.getComponent().getName());
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
			ChannelModel channel = controller.getModel().getChannelListModel().getChannel(e.getComponent().getName());
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
		if (e.getWheelRotation() < 0) controller.getView().getEmbeddedPlayerView().setVolume(controller.getView().getEmbeddedPlayerView().getVolume() + 10); 
		else controller.getView().getEmbeddedPlayerView().setVolume(controller.getView().getEmbeddedPlayerView().getVolume() - 10);
	}
	
}
