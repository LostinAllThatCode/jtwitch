package org.gdesign.jtwitch.player.gui.controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.gui.model.ChannelListModel;
import org.gdesign.jtwitch.player.gui.model.ChannelModel;
import org.gdesign.jtwitch.player.gui.model.MainModel;

import org.gdesign.jtwitch.player.gui.view.ChannelView;
import org.gdesign.jtwitch.player.gui.view.EmbeddedPlayerView;
import org.gdesign.jtwitch.player.gui.view.MainView;

public class MainController implements PropertyChangeListener{
	
	public MainView view;
	public MainModel model;
	
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
		LogManager.getLogger().trace(evt);
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
		}
	}
		
	class ChannelMouseListener extends MouseAdapter {
		
		Color colorLive = new Color(80,40,180);
		Color colorOff	= Color.DARK_GRAY;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().isEnabled() && e.getComponent().getClass().equals(ChannelView.class)){
				ChannelModel channel = model.getChannel(e.getComponent().getName());
				if (channel.isOnline()){
					e.getComponent().setEnabled(false);
					String mrl = model.getPlayerModel().startInstance("twitch.tv/"+channel.getName(),"source");
					String mediaOptions = "--network-cache=5000";
					view.getPlayerView().playMedia(mrl, mediaOptions);
					e.getComponent().setEnabled(true);
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
			LogManager.getLogger().debug(e.getComponent().getClass());
			if (e.getWheelRotation() < 0) view.getPlayerView().setVolume(view.getPlayerView().getVolume() + 10); 
			else view.getPlayerView().setVolume(view.getPlayerView().getVolume() - 10);
		}
	}

}
