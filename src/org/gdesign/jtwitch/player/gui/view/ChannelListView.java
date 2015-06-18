package org.gdesign.jtwitch.player.gui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gdesign.jtwitch.player.gui.model.ChannelModel;

public class ChannelListView extends JScrollPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4246339204285816080L;
	
	private JPanel channelList;

	public ChannelListView() {
		
		channelList = new JPanel();
		channelList.setLayout(new BoxLayout(channelList, BoxLayout.Y_AXIS));
		channelList.setBackground(Color.DARK_GRAY);
		
        setViewportView(channelList);
        setBorder(BorderFactory.createEmptyBorder());
        
        getVerticalScrollBar().setPreferredSize(new Dimension(5, Integer.MAX_VALUE));
        getHorizontalScrollBar().setPreferredSize(new Dimension(Integer.MAX_VALUE, 0));
        getVerticalScrollBar().setUnitIncrement(10);
        getVerticalScrollBar().setUI(new ChannelListScrollBarView());   
        
		setMinimumSize(new Dimension(250,Integer.MAX_VALUE));
		setPreferredSize(new Dimension(250,Integer.MAX_VALUE));
		setMaximumSize(new Dimension(250,Integer.MAX_VALUE));
	}
	
	
	public ChannelView createChannel(){
		return new ChannelView();
	}
	
	public void removeChannel(String channelName){
		for (Component c : channelList.getComponents()) {
			if (c.getName().compareTo(channelName) == 0) channelList.remove(c);
		}
	}
	
	public ChannelView getChannel(String channelName){
		for (Component c : channelList.getComponents()) {
			if (c.getName().compareTo(channelName) == 0) return (ChannelView) c;
		}
		return null;
	}
	
	public void addChannel(ChannelView view){
		view.addMouseListener(getMouseListeners()[0]);
		channelList.add(view);
	}
	
	public synchronized void sortChannels(Collection<ChannelModel> channels){
		Component[] comps = channelList.getComponents();
		channelList.removeAll();
		for (ChannelModel m : channels){
			for (Component c : comps){
				if (c.getName().compareTo(m.getName()) == 0) channelList.add(c);
			}
		}
		channelList.validate();
	}
	
}
