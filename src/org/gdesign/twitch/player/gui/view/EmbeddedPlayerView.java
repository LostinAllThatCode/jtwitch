package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gdesign.twitch.player.gui.view.EmbeddedPlayerControlView.Control;
import org.gdesign.utils.SystemInfo;
import org.gdesign.utils.SystemInfo.OperatingSystem;

import uk.co.caprica.vlcj.component.*;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;

public class EmbeddedPlayerView extends JPanel{

	private EmbeddedMediaPlayerComponent embeddedPlayer;
	private static final long serialVersionUID = 1L;	
	private int maxVolume,defVolume;
	private JPanel controls;
	
	public EmbeddedPlayerView(final JFrame j){
		setLayout(new BorderLayout());
		maxVolume = 200;
		defVolume = maxVolume/4;
		
		embeddedPlayer = new EmbeddedMediaPlayerComponent(){
			private static final long serialVersionUID = 1L;	
			protected FullScreenStrategy onGetFullScreenStrategy() {
				if (SystemInfo.getOS().equals(OperatingSystem.WIN)) return new Win32FullScreenStrategy(j);
				else if (SystemInfo.getOS().equals(OperatingSystem.UNIX)) return new XFullScreenStrategy(j);
				else return null;
			};		
			@Override
			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
			  mediaPlayer.setVolume(defVolume);
			}
		};
		
		controls = new JPanel();
		controls.setBackground(Color.DARK_GRAY);
		controls.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		EmbeddedPlayerControlView playstop = new EmbeddedPlayerControlView(Control.PLAY_STOP);
		EmbeddedPlayerControlView fullscreen = new EmbeddedPlayerControlView(Control.FULLSCREEN);
		fullscreen.setActive(true);
		
		EmbeddedPlayerControlView vol = new EmbeddedPlayerControlView(Control.VOLUME);
		vol.setValue(defVolume);
		vol.setActive(true);
		
		EmbeddedPlayerControlView status = new EmbeddedPlayerControlView(Control.STATUS);
		status.setValue(" ");
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		controls.add(playstop,c);
		c.weightx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.gridy = 0;
		controls.add(status,c);
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 0;
		controls.add(vol,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 3;
		c.gridy = 0;
		controls.add(fullscreen,c);
		
		this.add(embeddedPlayer,BorderLayout.CENTER);
		this.add(controls,BorderLayout.PAGE_END);
	}
	
	public EmbeddedPlayerControlView getControl(String name){
		for (Component c : controls.getComponents()) {
			if (c.getName() != null){
				if (name.compareTo(c.getName()) == 0) return (EmbeddedPlayerControlView) c;
			}
		}
		return null;
	}
	
	public void setControlValue(String control, Object value){
		getControl(control).setValue(value);
	}
	
	public void playMedia(String mrl, String mediaOptions){
		embeddedPlayer.getMediaPlayer().playMedia(mrl, mediaOptions);
		getControl("PLAY_STOP").setActive(true);
	}
	
	public boolean isPlaying(){
		return embeddedPlayer.getMediaPlayer().isPlaying();
	}
	
	public void stopMedia(){
		embeddedPlayer.getMediaPlayer().stop();
		getControl("PLAY_STOP").setActive(false);
	}
	
	public void setVolume(int value){
		setControlValue("VOLUME", (value <= maxVolume+10 ? value : maxVolume));
		embeddedPlayer.getMediaPlayer().setVolume(defVolume = (value <= maxVolume+10 ? value : maxVolume));
	}
	
	public int getVolume(){
		return embeddedPlayer.getMediaPlayer().getVolume();
	}

	public void toggleFullscreen() {
		embeddedPlayer.getMediaPlayer().toggleFullScreen();	
	}
	
	public boolean isFullscreen(){
		return embeddedPlayer.getMediaPlayer().isFullScreen();
	}
	
	@Override
	public synchronized void addMouseListener(MouseListener l) {
		for (Component c : controls.getComponents()) c.addMouseListener(l);
	}

}
