package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainView extends JPanel {

	private static final long serialVersionUID = -4716184426392503733L;

	private ChannelListView channelListview;
	private EmbeddedPlayerView playerView;

	public MainView(JFrame frame) {
		setLayout(new BorderLayout());
		channelListview = new ChannelListView();
		playerView = new EmbeddedPlayerView(frame);

		add(channelListview, BorderLayout.WEST);
		add(playerView, BorderLayout.CENTER);
		frame.add(this);
	}

	public ChannelListView getChannelListView() {
		return channelListview;
	}

	public EmbeddedPlayerView getEmbeddedPlayerView() {
		return playerView;
	}

	
	@Override
	public synchronized void addKeyListener(KeyListener l) {
		super.addKeyListener(l);
		channelListview.addKeyListener(l);
		playerView.addKeyListener(l);
	}

	@Override
	public void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		channelListview.addMouseListener(l);
		playerView.addMouseListener(l);
	}
	
	public void addActionListener(ActionListener l){
		channelListview.addActionListener(l);
	}


}
