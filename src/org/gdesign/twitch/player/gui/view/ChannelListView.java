package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gdesign.twitch.player.gui.model.ChannelModel;
import org.gdesign.twitch.player.gui.view.layout.channel.ChannelLayoutStrategy;
import org.gdesign.twitch.player.gui.view.layout.channellist.ChannelListLayoutStrategy;

public class ChannelListView extends JPanel {

	private static final long serialVersionUID = -4246339204285816080L;
	
	private JPanel channelList;
	private MenuBar menuBar;
	private JScrollPane scrollPane;
	private ChannelLayoutStrategy channelLayout;
	private ChannelListLayoutStrategy channelListLayout;
	private boolean showOfflineStreams;
	
	public ChannelListView() {
		setLayout(new BorderLayout());
		
		showOfflineStreams = true;
		
		menuBar = new MenuBar();
		
		channelList = new JPanel();
		channelList.setLayout(new BoxLayout(channelList, BoxLayout.Y_AXIS));
		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUI(new ChannelListScrollBarView());		
		scrollPane.setViewportView(channelList);
		scrollPane.setAutoscrolls(true);
		scrollPane.setDoubleBuffered(true);
		
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		add(menuBar,BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);
	}
	
	private void doCustomLayout(){
		channelList.setBackground(channelListLayout.getBackground());
		
		scrollPane.getVerticalScrollBar().setBackground(channelListLayout.getVerticalScrollBarColor());
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(channelListLayout.getVerticalScrollBarWidth(), Integer.MAX_VALUE));
		scrollPane.getHorizontalScrollBar().setBackground(channelListLayout.getHorizontalScrollBarColor());
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, channelListLayout.getHorizontalScrollBarHeight()));
		scrollPane.getVerticalScrollBar().setUnitIncrement(channelListLayout.getScrollbarUnitIncrement());
		
		setMinimumSize(channelListLayout.getDimension());
		setPreferredSize(channelListLayout.getDimension());
		setMaximumSize(channelListLayout.getDimension());
	}
	
	public void setChannelLayoutStrategy(ChannelLayoutStrategy strategy){
		this.channelLayout = strategy;
		for (Component c : channelList.getComponents()) {
			if (c.getClass().equals(ChannelView.class)) ((ChannelView)c).setLayoutStrategy(strategy);
		}
	}
	
	public void setChannelListLayoutStrategy(ChannelListLayoutStrategy strategy){
		this.channelListLayout = strategy;
		doCustomLayout();
	}

	public boolean showOfflineStreams(){
		return showOfflineStreams;
	}
	
	public void showOfflineStreams(boolean show){
		this.showOfflineStreams = show;
		for (Component c : channelList.getComponents()) {
			if (c.getClass().equals(ChannelView.class)){
				ChannelView v = (ChannelView) c;
				if (showOfflineStreams) {
					v.setVisible(true);
				} else {
					if (v.online) v.setVisible(true); else v.setVisible(false);
				}
			}	
		}	
		
	}
	
	public MenuBar getMenuBar(){
		return menuBar;
	}
	
	public ChannelView createChannel(String name){
		ChannelView v = new ChannelView(name,channelLayout);
		channelList.add(v);
		return v;
	}
	
	public void removeChannel(String channelName) {
		for (Component c : channelList.getComponents()) {
			if (c.getName().compareTo(channelName) == 0)
				channelList.remove(c);
		}
	}

	public ChannelView getChannel(String channelName) {
		for (Component c : channelList.getComponents()) {
			if (c.getName().compareTo(channelName) == 0)
				return (ChannelView) c;
		}
		return null;
	}

	public synchronized void sortChannels(Collection<ChannelModel> channels) {
		Component[] comps = channelList.getComponents();
		channelList.removeAll();
		if (channels != null) {
			for (ChannelModel m : channels) {
				for (Component c : comps) {
					if (c.getClass().equals(ChannelView.class) && c.getName().compareTo(m.getName()) == 0){
						channelList.add(c);
					}	
				}
			}
		}
	}
	
	public void addActionListener(ActionListener l){
		menuBar.addActionListener(l);
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		scrollPane.getVerticalScrollBar().addMouseListener(l);
	}
}
