package org.gdesign.jtwitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class MainView extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4716184426392503733L;

	private ChannelListView channelListview;
	private EmbeddedPlayerView playerView;

	public MainView() {
		setLayout(new BorderLayout());
		setTitle("JTwitch Player (0.1.alhpa)");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1280, 640);
		
		channelListview 	= new ChannelListView();
		playerView 			= new EmbeddedPlayerView(this);
		
		add(channelListview,BorderLayout.WEST);
		add(playerView,BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		setVisible(true);
	
	}
	
	public ChannelListView getChannelListView(){
		return channelListview;
	}
	
	
	public EmbeddedPlayerView getPlayerView(){
		return playerView;
	}
	
	@Override
	public void addMouseListener(MouseListener l) {
		channelListview.addMouseListener(l);
		playerView.addMouseListener(l);
	}
	
}
