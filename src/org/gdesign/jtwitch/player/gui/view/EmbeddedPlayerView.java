package org.gdesign.jtwitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private JButton qualityAudio,qualityLow,qualityMedium,qualityHigh,qualitySource;

	private JLabel info;
	
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
		
		JPanel controls = new JPanel();
		controls.setBackground(Color.DARK_GRAY);
		controls.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		controls.add(info = new JLabel("> "));
		info.setFont(new Font("Arial Unicode MS",Font.PLAIN,9));
		info.setForeground(Color.LIGHT_GRAY);
		info.setName("controlFullscreen");
		
		qualityAudio = new JButton("AUDIO");
		qualityAudio.setName("qualityAudio");
		qualityAudio.setBackground(Color.DARK_GRAY.brighter());
		qualityAudio.setBorderPainted(false);
		qualityAudio.setContentAreaFilled(true);
		qualityAudio.setForeground(Color.DARK_GRAY);
		qualityAudio.setBorder(BorderFactory.createEmptyBorder());
		qualityAudio.setFont(new Font("Arial Unicode MS",Font.PLAIN,9));
		qualityAudio.setEnabled(false);
		qualityLow = new JButton("LOW");
		qualityLow.setName("qualityLow");
		qualityLow.setBackground(Color.DARK_GRAY.brighter());
		qualityLow.setBorderPainted(false);
		qualityLow.setContentAreaFilled(true);
		qualityLow.setForeground(Color.DARK_GRAY);
		qualityLow.setBorder(BorderFactory.createEmptyBorder());
		qualityLow.setFont(new Font("Arial Unicode MS",Font.PLAIN,9));
		qualityLow.setEnabled(false);
		qualityMedium = new JButton("MEDIUM");
		qualityMedium.setName("qualityMedium");
		qualityMedium.setBackground(Color.DARK_GRAY.brighter());
		qualityMedium.setBorderPainted(false);
		qualityMedium.setContentAreaFilled(true);
		qualityMedium.setForeground(Color.DARK_GRAY);
		qualityMedium.setBorder(BorderFactory.createEmptyBorder());
		qualityMedium.setFont(new Font("Arial Unicode MS",Font.PLAIN,9));
		qualityMedium.setEnabled(false);
		qualityHigh = new JButton("HIGH");
		qualityHigh.setName("qualityHigh");
		qualityHigh.setBackground(Color.DARK_GRAY.brighter());
		qualityHigh.setBorderPainted(false);
		qualityHigh.setContentAreaFilled(true);
		qualityHigh.setForeground(Color.DARK_GRAY);
		qualityHigh.setBorder(BorderFactory.createEmptyBorder());
		qualityHigh.setFont(new Font("Arial Unicode MS",Font.PLAIN,9));
		qualityHigh.setEnabled(false);
		qualitySource = new JButton("SOURCE");
		qualitySource.setName("qualitySource");
		qualitySource.setBackground(Color.DARK_GRAY.brighter());
		qualitySource.setBorderPainted(false);
		qualitySource.setContentAreaFilled(true);
		qualitySource.setForeground(Color.DARK_GRAY);
		qualitySource.setBorder(BorderFactory.createEmptyBorder());
		qualitySource.setFont(new Font("Arial Unicode MS",Font.PLAIN,9));
		qualitySource.setEnabled(false);
		controls.setVisible(true);
		
		controls.add(Box.createHorizontalStrut(120));
		controls.add(qualityAudio);
		controls.add(qualityLow);
		controls.add(qualityMedium);
		controls.add(qualityHigh);
		controls.add(qualitySource);
		
		this.add(embeddedPlayer,BorderLayout.CENTER);
		this.add(controls,BorderLayout.PAGE_END);
	}
	
	public void setQuality(String quality){
		qualityAudio.setEnabled(true);
		qualityAudio.setForeground(Color.DARK_GRAY);
		qualityLow.setEnabled(true);
		qualityLow.setForeground(Color.DARK_GRAY);
		qualityMedium.setEnabled(true);
		qualityMedium.setForeground(Color.DARK_GRAY);
		qualityHigh.setEnabled(true);
		qualityHigh.setForeground(Color.DARK_GRAY);
		qualitySource.setEnabled(true);
		qualitySource.setForeground(Color.DARK_GRAY);
		switch (quality) {
		case "audio":
			qualityAudio.setForeground(Color.white);
			qualityAudio.setEnabled(false);
			break;
		case "low":
			qualityLow.setForeground(Color.white);
			qualityLow.setEnabled(false);
			break;
		case "medium":
			qualityMedium.setForeground(Color.white);
			qualityMedium.setEnabled(false);
			break;
		case "high":
			qualityHigh.setForeground(Color.white);
			qualityHigh.setEnabled(false);
			break;
		case "source":
			qualitySource.setForeground(Color.white);
			qualitySource.setEnabled(false);
			break;
		default:
			break;
		}
	}
	
	public void playMedia(String mrl, String mediaOptions){
		embeddedPlayer.getMediaPlayer().playMedia(mrl, mediaOptions);
	}
	
	public void stopMedia(){
		embeddedPlayer.getMediaPlayer().stop();
	}
	
	public void setVolume(int value){
		embeddedPlayer.getMediaPlayer().setVolume(defVolume = (value >= maxVolume ? maxVolume : value));
	}
	
	public int getVolume(){
		return embeddedPlayer.getMediaPlayer().getVolume();
	}
	
	public void setDescription(String description){
		info.setText(description);
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
		info.addMouseListener(l);
		mouseClickOverlay.addMouseListener(l);
		qualityAudio.addMouseListener(l);
		qualityLow.addMouseListener(l);
		qualityMedium.addMouseListener(l);
		qualityHigh.addMouseListener(l);
		qualitySource.addMouseListener(l);
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
