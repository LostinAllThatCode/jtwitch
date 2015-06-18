package org.gdesign.jtwitch.player.gui.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.gdesign.jtwitch.player.gui.model.ChannelListModel;
import org.gdesign.jtwitch.player.gui.model.ChannelModel;
import org.gdesign.jtwitch.player.gui.model.EmbeddedPlayerModel;
import org.gdesign.jtwitch.player.gui.model.MainModel;
import org.gdesign.jtwitch.player.gui.view.ChannelView;
import org.gdesign.jtwitch.player.gui.view.EmbeddedPlayerView;
import org.gdesign.jtwitch.player.gui.view.MainView;
import org.gdesign.jtwitch.player.livestreamer.Livestreamer;
import org.gdesign.jtwitch.player.livestreamer.LivestreamerFactory;

public class MainController implements PropertyChangeListener{
	
	private MainView view;
	private MainModel model;
	
	public MainController(MainView view, MainModel model) {
		this.view = view;
		this.model = model;
		
		ChannelMouseListener listener = new ChannelMouseListener();
		
		this.view.addMouseListener(listener);
		this.view.addMouseWheelListener(listener);
		
		this.model.addModelListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource().getClass().equals(ChannelListModel.class)){
			if (evt.getPropertyName().compareTo("addChannel") == 0){
				ChannelModel cm = (ChannelModel) evt.getNewValue();
				ChannelView cv = new ChannelView();
				if (cm != null && cv != null) {
					cv.setChannelName(cm.getName());
					cv.setChannelGame(cm.getGame());
					cv.setChannelViewers(String.valueOf(cm.getViewers()));
					cv.setChannelAction(cm.getAction());
					cv.setOnline(cm.isOnline());
					view.getChannelListView().addChannel(cv);
				}
			}
		} else if (evt.getSource().getClass().equals(ChannelModel.class)){
			if (evt.getNewValue() != null){
				ChannelModel cm = (ChannelModel) evt.getSource();
				ChannelView cv 	= view.getChannelListView().getChannel(cm.getName());
				if (cm != null && cv != null) {
					if (evt.getPropertyName().equals("gameChanged")) cv.setChannelGame((String) evt.getNewValue());
					else if (evt.getPropertyName().equals("viewersChanged")) cv.setChannelViewers(evt.getNewValue().toString());
					else if (evt.getPropertyName().equals("actionChanged")) cv.setChannelAction((String) evt.getNewValue());
					else if (evt.getPropertyName().equals("stateChanged")) cv.setOnline((boolean) evt.getNewValue());
					else if (evt.getPropertyName().equals("sortChannels")) view.getChannelListView().sortChannels(model.getSortedChannels());
				}
			}
		} else if (evt.getSource().getClass().equals(EmbeddedPlayerModel.class)){
			if (evt.getNewValue() != null){
				EmbeddedPlayerModel pm = model.getPlayerModel();
				EmbeddedPlayerView pv = view.getPlayerView();
				if (pv != null && pm != null){
					if (evt.getPropertyName().equals("streamStarted")) pv.setDescription((String) evt.getNewValue());
				}
			}
		}
	}
		
	class ChannelMouseListener extends MouseAdapter {
		
		Color colorLive = new Color(80,40,180);
		Color colorOff	= Color.DARK_GRAY;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannel(e.getComponent().getName());
				Livestreamer streamer = model.getPlayerModel().getInstance();
				if (channel.isOnline()){
					if (streamer == null){
						e.getComponent().setEnabled(false);
						String mrl = model.getPlayerModel().startInstance("twitch.tv/"+channel.getName(),LivestreamerFactory.getDefaultQuality());
						String mediaOptions = "--network-cache=5000";
						if (mrl != null) {
							view.getPlayerView().playMedia(mrl, mediaOptions);
							e.getComponent().setEnabled(true);
						}
					} else {
						if (!streamer.getStream().equals("twitch.tv/"+channel.getName())) {
							LivestreamerFactory.removeInstance(streamer);
							e.getComponent().setEnabled(false);
							String mrl = model.getPlayerModel().startInstance("twitch.tv/"+channel.getName(),LivestreamerFactory.getDefaultQuality());
							String mediaOptions = "--network-cache=5000";
							if (mrl != null) {
								view.getPlayerView().playMedia(mrl, mediaOptions);
								e.getComponent().setEnabled(true);
							}
						}
					}
				}
			} else if (e.getComponent().isEnabled() && 
					(e.getComponent().getName().equals("controlFullscreen") || e.getComponent().getClass().equals(EmbeddedPlayerView.MouseClickOverlay.class)) ){
						if (e.getClickCount() >= 2) {
							view.getPlayerView().toggleFullscreen();
							if (view.getPlayerView().isFullscreen()) {
								view.getChannelListView().setVisible(false);
							} else {
								view.getChannelListView().setVisible(true);
							}
						}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannel(e.getComponent().getName());
				if (channel.isOnline()) {
					e.getComponent().setBackground(colorLive.brighter());
					e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					e.getComponent().setBackground(colorOff.brighter());
				}
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannel(e.getComponent().getName());
				if (channel.isOnline()) e.getComponent().setBackground(colorLive);
				else e.getComponent().setBackground(colorOff);
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) view.getPlayerView().setVolume(view.getPlayerView().getVolume() + 10); 
			else view.getPlayerView().setVolume(view.getPlayerView().getVolume() - 10);
		}
	}

}
