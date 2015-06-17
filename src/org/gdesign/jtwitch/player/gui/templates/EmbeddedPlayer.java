package org.gdesign.jtwitch.player.gui.templates;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gdesign.jtwitch.player.livestreamer.Livestreamer;

import uk.co.caprica.vlcj.component.*;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

public class EmbeddedPlayer extends JPanel implements Observer{
	
	private EmbeddedMediaPlayerComponent embeddedPlayer;
	private Livestreamer livestreamer;
	private static final long serialVersionUID = 1L;

	public EmbeddedPlayer(){
		setLayout(new BorderLayout());

		livestreamer = new Livestreamer();
		livestreamer.addObserver(this);
		
		embeddedPlayer = new EmbeddedMediaPlayerComponent();
		embeddedPlayer.getMediaPlayer().enableOverlay(true);
		embeddedPlayer.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {
				super.playing(mediaPlayer);
			}
		});
		
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		controls.add(new JLabel("asd"));
		
	
		this.add(embeddedPlayer,BorderLayout.CENTER);
		this.add(controls,BorderLayout.PAGE_END);
	}
	
	public Livestreamer getLivestreamer(){
		return livestreamer;
	}

	@Override
	public void update(Observable o, Object arg) {
		String[] cmd = arg.toString().split("§");
		if (cmd[1].compareTo("start") == 0) livestreamer.openStream(cmd[0]);
		if (cmd[1].compareTo("play") == 0) embeddedPlayer.getMediaPlayer().playMedia(cmd[2],"--network-caching 5000");	
		if (cmd[1].compareTo("stop") == 0) embeddedPlayer.getMediaPlayer().stop();
		if (cmd[1].compareTo("volup") == 0) embeddedPlayer.getMediaPlayer().setVolume(embeddedPlayer.getMediaPlayer().getVolume()+10);
		if (cmd[1].compareTo("voldown") == 0) embeddedPlayer.getMediaPlayer().setVolume(embeddedPlayer.getMediaPlayer().getVolume()-10);
	}
}
