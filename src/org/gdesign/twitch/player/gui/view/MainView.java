package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.gdesign.twitch.player.gui.view.embeddedplayer.EmbeddedPlayerView;
import org.gdesign.twitch.player.gui.view.streams.StreamListView;
import org.gdesign.twitch.player.gui.view.transparent.MessagePopupFactory;

public class MainView extends JPanel {

	private static final long serialVersionUID = -4716184426392503733L;

	private StreamListView streamListView;
	private EmbeddedPlayerView embeddebPlayerView;
	private JFrame parent;
	
	public MainView(JFrame frame) {
		this.parent = frame;
		this.parent.add(this);
		
		this.setLayout(new BorderLayout());		
		this.streamListView = new StreamListView();
		this.embeddebPlayerView = new EmbeddedPlayerView(parent);	
		this.add(embeddebPlayerView,BorderLayout.CENTER);
		
		this.setStreamListPosition(BorderLayout.WEST);
		
		MessagePopupFactory.initialize(parent);
	}

	public StreamListView getStreamList() {
		return streamListView;
	}

	public EmbeddedPlayerView getEmbeddedPlayer() {
		return embeddebPlayerView;
	}
	public void setStreamListPosition(String alignment){
		this.remove(streamListView);
		this.add(streamListView,alignment);
		this.validate();
		this.repaint();
	}

	@Override
	public void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		streamListView.addMouseListener(l);
		embeddebPlayerView.addMouseListener(l);
	}
	
	public void addActionListener(ActionListener l){
		streamListView.addActionListener(l);
	}


}
