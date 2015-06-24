package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
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
	private Window mouseClickOverlay;
	private int maxVolume,defVolume;
	private JPanel controls;
	
	public EmbeddedPlayerView(final JFrame j){
		setLayout(new BorderLayout());
		
		defVolume = 15;
		maxVolume = 200;
		
		mouseClickOverlay = new MouseClickOverlay(j);
		
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
		
		embeddedPlayer.getMediaPlayer().setOverlay(mouseClickOverlay);
		
		controls = new JPanel();
		controls.setBackground(Color.DARK_GRAY);
		controls.setLayout(new FlowLayout(FlowLayout.LEFT));		
		controls.add(new EmbeddedPlayerControlView(Control.PLAY_STOP));
		
		this.add(embeddedPlayer,BorderLayout.CENTER);
		this.add(controls,BorderLayout.PAGE_END);
	}
	
	public void playMedia(String mrl, String mediaOptions){
		embeddedPlayer.getMediaPlayer().playMedia(mrl, mediaOptions);
		((EmbeddedPlayerControlView)controls.getComponent(0)).setRunning(true);
	}
	
	public boolean isPlaying(){
		return embeddedPlayer.getMediaPlayer().isPlaying();
	}
	
	public void stopMedia(){
		embeddedPlayer.getMediaPlayer().stop();
		((EmbeddedPlayerControlView)controls.getComponent(0)).setRunning(false);
	}
	
	public void setVolume(int value){
		embeddedPlayer.getMediaPlayer().setVolume(defVolume = (value >= maxVolume ? maxVolume : value));
	}
	
	public int getVolume(){
		return embeddedPlayer.getMediaPlayer().getVolume();
	}
	
	public void setDescription(String description){
		//info.setText(description);
	}

	public void toggleFullscreen() {
		embeddedPlayer.getMediaPlayer().toggleFullScreen();	
		mouseClickOverlay.pack();
	}
	
	public boolean isFullscreen(){
		return embeddedPlayer.getMediaPlayer().isFullScreen();
	}
	
	@Override
	public synchronized void addMouseListener(MouseListener l) {
		//info.addMouseListener(l);
		mouseClickOverlay.addMouseListener(l);
		for (Component c : controls.getComponents()) c.addMouseListener(l);
	}
	
	public class MouseClickOverlay extends Window {
		private static final long serialVersionUID = 1L;
		
		// TODO Fix resizing issue.	
		public MouseClickOverlay(Window owner) {
	        super(owner);
	        setBackground(new Color(0,0,0,0));
		}

	}
}
