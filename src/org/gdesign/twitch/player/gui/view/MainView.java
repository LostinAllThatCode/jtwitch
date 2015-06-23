package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainView extends JPanel{

	private static final long serialVersionUID = -4716184426392503733L;

	private ChannelListView channelListview;
	private EmbeddedPlayerView playerView;

	public MainView(JFrame frame) {
		setLayout(new BorderLayout());
		
		channelListview 	= new ChannelListView();
		playerView 			= new EmbeddedPlayerView(frame);
		
		add(channelListview,BorderLayout.WEST);
		add(playerView,BorderLayout.CENTER);
		
		frame.add(this);
	}
	
	public ChannelListView getChannelListView(){
		return channelListview;
	}
	
	public EmbeddedPlayerView getEmbeddedPlayerView(){
		return playerView;
	}
	
	@Override
	public void addMouseListener(MouseListener l) {
		channelListview.addMouseListener(l);
		playerView.addMouseListener(l);
	}
	
}
