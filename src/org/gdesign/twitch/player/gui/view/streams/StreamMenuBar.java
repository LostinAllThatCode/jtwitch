package org.gdesign.twitch.player.gui.view.streams;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class StreamMenuBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3935669875931200404L;

	private JMenuBar menuBar;

	private Dimension normal;
	
	public StreamMenuBar(StreamListView channelListView){
		this.setLayout(new BorderLayout());
		
		normal = new Dimension(250,39);
		
		setMinimumSize(normal);
		setPreferredSize(normal);
		setMaximumSize(normal);
		
		UIManager.put("MenuItem.selectionBackground", new Color(80, 40, 180));
		UIManager.put("MenuItem.selectionForeground", Color.WHITE);
		UIManager.put("Menu.selectionBackground", new Color(80, 40, 180));
		UIManager.put("Menu.selectionForeground", Color.WHITE);
		
		setBackground(Color.DARK_GRAY.darker());

		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBorder(BorderFactory.createEmptyBorder(0,-1,0,0));
		
		StreamMenu settings = new StreamMenu(this.getLocation());
		settings.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu1_bg.png")));
		settings.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu1_bg_hover.png")));
		settings.setRolloverIcon(settings.getIcon());
		settings.add(new StreamMenuItem("Authorize jTwitchPlayer"));
		settings.add(new StreamMenuItem(""));
		settings.add(new StreamMenuItem("Show following streams"));
		settings.add(new StreamMenuItem(""));
		//settings.add(new CustomMenuItem("Show featured streams"));
		settings.add(new StreamMenuItem("Show Top 25 streams"));
		settings.add(new StreamMenuItem("Show Top X streams..."));
		//settings.add(new CustomMenuItem("Show streams by game..."));	
		menuBar.add(settings);

		
		StreamMenu menu2 = new StreamMenu(this.getLocation());
		menu2.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg.png")));
		menu2.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg_hover.png")));
		menu2.setRolloverIcon(menu2.getIcon());
		menu2.setDisabledIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg.png")));
		menu2.setEnabled(false);
		menuBar.add(menu2);
		
		StreamMenu menu3 = new StreamMenu(this.getLocation());
		menu3.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg.png")));
		menu3.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg_hover.png")));
		menu3.setRolloverIcon(menu3.getIcon());
		menu3.setDisabledIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg.png")));
		//menu3.setEnabled(false);
		menu3.setActionCommand("ASD");
		menuBar.add(menu3);
		
		StreamMenu menu4 = new StreamMenu(this.getLocation());
		menu4.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu4_bg.png")));
		menu4.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu4_bg_hover.png")));
		menu4.setRolloverIcon(menu4.getIcon());
		menu4.add(new StreamMenuItem("Left sided streamlist"));
		menu4.add(new StreamMenuItem("Right sided streamlist"));
		
		menuBar.add(menu4);
		
		this.add(menuBar,BorderLayout.CENTER);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.DARK_GRAY.darker().darker());
		g2d.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
	}
	
	public void addActionListener(ActionListener l){
		for (Component d : menuBar.getComponents()){
			StreamMenu menu = (StreamMenu) d;
			menu.addActionListener(l);
			for (Component c : menu.getMenuComponents()) {
				StreamMenuItem i = (StreamMenuItem) c;
				i.addActionListener(l);
			}
		}
	}

}
