package org.gdesign.twitch.player.gui.view.streams;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JMenuItem;

public class StreamMenuItem extends JMenuItem{	
	private static final long serialVersionUID = 3267754878728171232L;
	
	public StreamMenuItem(String name) {
		super(name);
		setBorderPainted(false);
		setOpaque(true);
		if (name == "") {
			setBackground(Color.DARK_GRAY);
			setMinimumSize(new Dimension(250,1));
			setPreferredSize(new Dimension(250,1));
			setMaximumSize(new Dimension(250,1));
		} else {
			setFont(new Font("Arial", Font.BOLD, 11));
			setBackground(Color.DARK_GRAY.darker());
			setForeground(Color.LIGHT_GRAY);
			setActionCommand(name);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			setFont(new Font("Arial",Font.BOLD,10));
			setMinimumSize(new Dimension(250,22));
			setPreferredSize(new Dimension(250,22));
			setMaximumSize(new Dimension(250,22));
		}
		
	}		
}
