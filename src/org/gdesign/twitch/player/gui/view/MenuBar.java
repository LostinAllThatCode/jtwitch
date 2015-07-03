package org.gdesign.twitch.player.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public class MenuBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3935669875931200404L;

	private JMenuBar menuBar;
	private JLabel status;
	private Point popupLocation;
	
	public MenuBar(){
		this.setLayout(new BorderLayout());
		
		popupLocation = this.getLocation();
		
		UIManager.put("MenuItem.selectionBackground", new Color(80, 40, 180));
		UIManager.put("MenuItem.selectionForeground", Color.WHITE);
		UIManager.put("Menu.selectionBackground", new Color(80, 40, 180));
		UIManager.put("Menu.selectionForeground", Color.WHITE);
		
		setBackground(Color.DARK_GRAY.darker());
		setMinimumSize(new Dimension(250,39));
		setPreferredSize(new Dimension(250,39));
		setMaximumSize(new Dimension(250,60));
		
		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBorder(BorderFactory.createEmptyBorder(0,-1,0,0));
		
		CustomMenu settings = new CustomMenu();
		settings.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu1_bg.png")));
		settings.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu1_bg_hover.png")));
		settings.setRolloverIcon(settings.getIcon());
		settings.add(new CustomMenuItem("Account..."));
		settings.add(new CustomMenuItem(""));
		settings.add(new CustomMenuItem("Show following streams"));
		settings.add(new CustomMenuItem("Show featured streams"));
		settings.add(new CustomMenuItem("Show top 20 streams"));
		settings.add(new CustomMenuItem("Show streams by game..."));	
		menuBar.add(settings);
		
		CustomMenu menu2 = new CustomMenu();
		menu2.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg.png")));
		menu2.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg_hover.png")));
		menu2.setRolloverIcon(menu2.getIcon());
		menuBar.add(menu2);
		
		CustomMenu menu3 = new CustomMenu();
		menu3.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg.png")));
		menu3.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu_bg_hover.png")));
		menu3.setRolloverIcon(menu3.getIcon());
		menuBar.add(menu3);
		
		CustomMenu menu4 = new CustomMenu();
		menu4.setIcon(new ImageIcon(ClassLoader.getSystemResource("menu4_bg.png")));
		menu4.setPressedIcon(new ImageIcon(ClassLoader.getSystemResource("menu4_bg_hover.png")));
		menu4.setRolloverIcon(menu4.getIcon());
		menu4.add(new CustomMenuItem("Hide offline streams"));
		menu4.add(new CustomMenuItem(""));
		menu4.add(new CustomMenuItem("Small channels"));
		menu4.add(new CustomMenuItem("Normal channels"));
		
		menuBar.add(menu4);
		
		status = new JLabel("test");
		status.setFont(new Font("Arial", Font.PLAIN, 10));
		status.setOpaque(true);
		status.setBackground(new Color(41,41,41));
		status.setForeground(Color.DARK_GRAY);
		status.setMinimumSize(new Dimension(63, 21));
		status.setPreferredSize(new Dimension(63, 21));
		status.setMaximumSize(new Dimension(63, 21));
		status.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
		status.setVisible(false);
		
		
		this.add(menuBar,BorderLayout.CENTER);
		this.add(status,BorderLayout.SOUTH);
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
			CustomMenu menu = (CustomMenu) d;
			for (Component c : menu.getMenuComponents()) {
				CustomMenuItem i = (CustomMenuItem) c;
				i.addActionListener(l);
			}
		}
	}
	
	public class CustomMenu extends JMenu{
		private static final long serialVersionUID = -5377710903251690945L;

		
		public CustomMenu() {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			getPopupMenu().setBorder(BorderFactory.createLineBorder(new Color(80, 40, 180).brighter(), 1));
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
					CustomMenu menu = (CustomMenu) e.getSource();
					menu.setIcon(menu.getPressedIcon());
					setMenuLocation(popupLocation.x-getLocation().x, popupLocation.y+38);
				}
				@Override
				public void menuDeselected(MenuEvent e) {
					CustomMenu menu = (CustomMenu) e.getSource();
					menu.setIcon(menu.getRolloverIcon());
				}
				@Override
				public void menuCanceled(MenuEvent e) {
					CustomMenu menu = (CustomMenu) e.getSource();
					menu.setIcon(menu.getRolloverIcon());
				}
			});
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					CustomMenu menu = (CustomMenu) e.getSource();
					menu.setIcon(menu.getPressedIcon());					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					CustomMenu menu = (CustomMenu) e.getSource();
					if (!isSelected()) menu.setIcon(menu.getRolloverIcon());
				}
			});
			
			
		}
		
		
	}
	
	public class CustomMenuItem extends JMenuItem{	
		private static final long serialVersionUID = 3267754878728171232L;
		
		public CustomMenuItem(String name) {
			super(name);
			setBorderPainted(false);
			setOpaque(true);
			if (name == "") {
				setBackground(Color.DARK_GRAY);
				setMinimumSize(new Dimension(248,1));
				setPreferredSize(new Dimension(248,1));
				setMaximumSize(new Dimension(248,1));
			} else {
				setFont(new Font("Arial", Font.BOLD, 11));
				setBackground(Color.DARK_GRAY.darker());
				setForeground(Color.LIGHT_GRAY);
				setActionCommand(name);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				setFont(new Font("Arial",Font.BOLD,10));
				setMinimumSize(new Dimension(248,22));
				setPreferredSize(new Dimension(248,22));
				setMaximumSize(new Dimension(248,22));
			}

		}		
	}
}
