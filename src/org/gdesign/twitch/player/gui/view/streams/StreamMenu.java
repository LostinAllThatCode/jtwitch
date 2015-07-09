package org.gdesign.twitch.player.gui.view.streams;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class StreamMenu extends JMenu{
	private static final long serialVersionUID = -5377710903251690945L;

	
	public StreamMenu(final Point popupLocation) {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getPopupMenu().setBorder(BorderFactory.createEmptyBorder());
		getPopupMenu().setBackground(Color.DARK_GRAY.darker());
		
		setFont(new Font("Arial", Font.BOLD, 11));
		setBackground(Color.DARK_GRAY.darker());
		setForeground(Color.WHITE);
		setMinimumSize(new Dimension(63, 39));
		setPreferredSize(new Dimension(63, 39));
		setMaximumSize(new Dimension(63, 39));
		setBorder(BorderFactory.createEmptyBorder(0, -4, 0, 0));
		setHorizontalTextPosition(SwingConstants.CENTER);
		setRolloverEnabled(true);
		
		addMenuListener(new MenuListener() {			
			@Override
			public void menuSelected(MenuEvent e) {
				StreamMenu menu = (StreamMenu) e.getSource();
				menu.setIcon(menu.getPressedIcon());
				setMenuLocation(popupLocation.x-getLocation().x, popupLocation.y+35);
			}
			@Override
			public void menuDeselected(MenuEvent e) {
				StreamMenu menu = (StreamMenu) e.getSource();
				menu.setIcon(menu.getRolloverIcon());
			}
			@Override
			public void menuCanceled(MenuEvent e) {
				StreamMenu menu = (StreamMenu) e.getSource();
				menu.setIcon(menu.getRolloverIcon());
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				StreamMenu menu = (StreamMenu) e.getSource();
				menu.setIcon(menu.getPressedIcon());
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				StreamMenu menu = (StreamMenu) e.getSource();
				if (!isSelected()) menu.setIcon(menu.getRolloverIcon());
			}
		});			
	}	
}