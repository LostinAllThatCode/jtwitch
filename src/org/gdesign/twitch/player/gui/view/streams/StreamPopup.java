package org.gdesign.twitch.player.gui.view.streams;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

public class StreamPopup extends JPopupMenu {

	private static final long serialVersionUID = -339043982921984520L;
	private StreamView streamView;
	
	public StreamPopup(Component c, MouseEvent e, ActionListener l) throws IOException {
		this.streamView = (StreamView) c;
		setBorder(BorderFactory.createEmptyBorder());
		
		StreamMenuItem item = new StreamMenuItem((streamView.isFollowing() ? "Unfollow" : "Follow"));
		item.setName(streamView.getName());
		item.setMinimumSize(new Dimension(80,21));
		item.setPreferredSize(new Dimension(80,21));
		item.setMaximumSize(new Dimension(80,21));
		item.addActionListener(l);
		
		StreamMenuItem sep = new StreamMenuItem("");
		sep.setMinimumSize(new Dimension(80,1));
		sep.setPreferredSize(new Dimension(80,1));
		sep.setMaximumSize(new Dimension(80,1));
			
		StreamMenuItem item2 = new StreamMenuItem("Chat");
		item2.setName(streamView.getName());
		item2.addActionListener(l);
		item2.setMinimumSize(new Dimension(80,21));
		item2.setPreferredSize(new Dimension(80,21));
		item2.setMaximumSize(new Dimension(80,21));
		
		add(item);
		add(sep);
		add(item2);
		
		show(c, e.getX() - getSize().width, e.getY());
	}

}
