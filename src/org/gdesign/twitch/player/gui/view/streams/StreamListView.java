package org.gdesign.twitch.player.gui.view.streams;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gdesign.twitch.api.TwitchAPI;
import org.gdesign.twitch.api.resource.streams.Stream;
import org.gdesign.twitch.api.resource.streams.StreamList;
import org.gdesign.twitch.player.gui.view.streams.layout.StreamListViewLayout;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;

public class StreamListView extends JPanel {

	private static final long serialVersionUID = -4246339204285816080L;
	
	private MouseAdapter mouseListener;
	private JPanel streamList;
	private StreamMenuBar menuBar;
	private JScrollPane scrollPane;
	private StreamListViewLayout layout;
	
	public StreamListView() {
		setLayout(new BorderLayout());
		
		menuBar = new StreamMenuBar(this);
		
		streamList = new JPanel();
		streamList.setLayout(new BoxLayout(streamList, BoxLayout.Y_AXIS));
		streamList.setDoubleBuffered(true);
				
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUI(new StreamListScrollBarView());		
		scrollPane.setViewportView(streamList);
		scrollPane.setAutoscrolls(false);
		scrollPane.setDoubleBuffered(true);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		add(menuBar,BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);	
	}
	
	private void doCustomLayout(){
		streamList.setBackground(layout.bg_color);
		
		scrollPane.getVerticalScrollBar().setBackground(layout.scrollbar_v_color);
		scrollPane.getVerticalScrollBar().setPreferredSize(layout.scrollbar_v_dim);
		scrollPane.getHorizontalScrollBar().setBackground(layout.scrollbar_h_color);
		scrollPane.getHorizontalScrollBar().setPreferredSize(layout.scrollbar_h_dim);
		scrollPane.getVerticalScrollBar().setUnitIncrement(layout.scrollbar_unit_inc);
	}
	
	public void setLayoutStrategy(StreamListViewLayout strategy){
		this.layout = strategy;
		for (Component c : streamList.getComponents()) {
			if (c.getClass().equals(StreamView.class)) ((StreamView)c).setLayoutStrategy(this.layout.stream);
		}
		doCustomLayout();
	}

	
	public StreamMenuBar getStreamMenuBar(){
		return menuBar;
	}
	
	public synchronized void setStreamList(StreamList list) {
		streamList.removeAll();
		repaint();
		for (final Stream stream : list.streams) {
			final StreamView view = new StreamView(this.layout.stream);
			view.setStreamName(stream.channel.name);
			view.setStreamDisplayName(stream.channel.display_name);
			view.setStreamGame(stream.game);
			view.setStreamViewers(String.valueOf(stream.viewers));
			view.setStreamIcon(stream.channel.logo);
			
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						if (TwitchAPI.isFollowing(stream.channel.name)){
							view.setFollowing(true);
						}
					} catch (IOException e) {
						view.setFollowing(false);
					}
				}
			}).start();
			
			view.addMouseListener(mouseListener);
			view.setVisible(true);
			view.setEnabled(true);
			
			if (LivestreamerFactory.getRunningStreamsChannelname().compareTo(stream.channel.name) == 0)
				view.setStreamPlaying(true);
			
			view.doCustomLayout();
			streamList.add(view);
		}
	}
	
	public void setStreamPlaying(String channelName, boolean playing) {
		for (Component c : streamList.getComponents()) {
			if (c.getName().compareTo(channelName) == 0) {
				if (c.getClass().equals(StreamView.class)) {
					((StreamView) c).setStreamPlaying(playing);
				}
			}
		}
	}
	
	public void removeStreamView(String channelName) {
		for (Component c : streamList.getComponents()) {
			if (c.getName().compareTo(channelName)==0) {
				streamList.remove(c);
				break;
			}
		}
		validate();
		repaint();
	}
	
	public void addActionListener(ActionListener l){
		menuBar.addActionListener(l);
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		mouseListener = (MouseAdapter) l;
		menuBar.addMouseMotionListener(mouseListener);
	}
}
