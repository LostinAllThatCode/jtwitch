package org.gdesign.jtwitch.player.gui.listener;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.gui.controller.MainController;
import org.gdesign.jtwitch.player.gui.model.ChannelModel;
import org.gdesign.jtwitch.player.gui.view.ChannelView;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerInstance;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerAlreadyRunningException;
import org.gdesign.jtwitch.player.livestreamer.exception.LivestreamerConnectionTimeoutException;

public class ChannelMouseListener extends MouseAdapter {
	
	private MainController controller;
	private Color colorLive = new Color(80,40,180);
	
	public ChannelMouseListener(MainController controller) {
		this.controller = controller;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
			ChannelModel channel = controller.getModel().getChannelListModel().getChannel(e.getComponent().getName());
			if (channel.isOnline()){
				try {
					LivestreamerInstance instance = LivestreamerFactory.startInstance("twitch.tv/"+channel.getName(), LivestreamerFactory.getDefaultQuality());
					controller.getView().getPlayerView().playMedia(instance.getMRL(), "--network-cache=5000");
					if (instance.waitForClientConnection()) controller.getModel().getEmbeddedPlayerModel().set(instance);
				} catch (LivestreamerAlreadyRunningException | InterruptedException | LivestreamerConnectionTimeoutException e1) {
					LogManager.getLogger().error(e1);
				}
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
			ChannelModel channel = controller.getModel().getChannelListModel().getChannel(e.getComponent().getName());
			if (channel.isOnline()) {
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				e.getComponent().setBackground(colorLive.brighter());
			}
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
			ChannelModel channel = controller.getModel().getChannelListModel().getChannel(e.getComponent().getName());
			if (channel.isOnline()) {
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				e.getComponent().setBackground(colorLive);
			}			
		}
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) controller.getView().getPlayerView().setVolume(controller.getView().getPlayerView().getVolume() + 10); 
		else controller.getView().getPlayerView().setVolume(controller.getView().getPlayerView().getVolume() - 10);
	}
	
}
