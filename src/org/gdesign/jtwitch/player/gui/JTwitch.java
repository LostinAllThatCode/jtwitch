package org.gdesign.jtwitch.player.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Observable;
import java.util.Properties;

import javax.swing.*;
import org.apache.logging.log4j.LogManager;

import org.gdesign.jtwitch.player.gui.templates.Channel;
import org.gdesign.jtwitch.player.gui.templates.ChannelScrollBar;
import org.gdesign.jtwitch.player.gui.templates.EmbeddedPlayer;

import org.gdesign.twitch.api.prototypes.TChannel;
import org.gdesign.twitch.api.prototypes.TStream;
import org.gdesign.twitch.api.util.TwitchAPI;
import org.gdesign.utils.Configuration;


import org.json.simple.parser.ParseException;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class JTwitch extends Observable{
	
	protected static Properties config;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","true");
		System.setProperty("swing.aatext", "true");
		
		config = new Configuration("jtwitch.properties");
		
        boolean found = new NativeDiscovery().discover();
        if (found) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new JTwitch();
                }
            });
        } else {
        	//NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), config.getProperty("videolan"));
        	LogManager.getLogger().error("Can't find vlc natives.");
        }
	}
	
    public JTwitch(){
		EmbeddedPlayer emp = new EmbeddedPlayer();
    	addObserver(emp);
    	
    	JFrame frame = new JFrame("JTwitch Player (0.1.alhpa)");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 1280, 640);
        frame.addMouseWheelListener(new MouseWheelListener() {				
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					setChanged();
					notifyObservers("§volup");
				} else {
					setChanged();
					notifyObservers("§voldown");
				}
			}
		});
        
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        JPanel channels = new JPanel();
        channels.setLayout(new BoxLayout(channels, BoxLayout.Y_AXIS));

        channels.setBackground(Color.DARK_GRAY);
        
        try {
			for (TChannel c : TwitchAPI.getFollows(config.getProperty("username")).getChannels()){
				final String channelName = c.get("display_name");	
				Channel componentChannel = new Channel(channelName);	
				
				addObserver(componentChannel);
				emp.getLivestreamer().addObserver(componentChannel);
				
				channels.add(componentChannel);
				
				TStream stream = TwitchAPI.getStream(channelName);
				if (stream.isOnline()){
					setChanged();
					notifyObservers(channelName + "§game§"+stream.get("game"));
					setChanged();
					notifyObservers(channelName + "§viewers§"+stream.get("viewers"));
					setChanged();
					notifyObservers(channelName + "§online");
					componentChannel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							super.mouseEntered(e);
							e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						
						@Override
						public void mouseExited(MouseEvent e) {
							super.mouseExited(e);
							e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							super.mouseClicked(e);
							setChanged();
							notifyObservers(channelName + "§start");
						}
					});
				}

			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
        
       
        
        frame.add(container);
        
        final JScrollPane s = new JScrollPane();
        s.setViewportView(channels);
        s.setBorder(BorderFactory.createEmptyBorder());
        s.getVerticalScrollBar().setPreferredSize(
        		new Dimension(5, Integer.MAX_VALUE));
        s.getHorizontalScrollBar().setPreferredSize(
        		new Dimension(Integer.MAX_VALUE, 0));
        s.getVerticalScrollBar().setUnitIncrement(10);
        
        s.getVerticalScrollBar().setUI(new ChannelScrollBar());
        
        container.add(s,BorderLayout.WEST);
        container.add(emp,BorderLayout.CENTER);
        
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosed(WindowEvent e) {
        		// TODO Auto-generated method stub
        		super.windowClosed(e);
        		setChanged();
        		notifyObservers("stop");
        	}
        	
		});
        frame.setLocationRelativeTo(null);
        frame.repaint();
        frame.setVisible(true);   	
        
    }


}
