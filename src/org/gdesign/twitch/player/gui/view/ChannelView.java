package org.gdesign.twitch.player.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.gdesign.utils.ResourceManager;

public class ChannelView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3633213310390485830L;
	
	private Color bgColorOffline 	= Color.DARK_GRAY.darker();
	private Color bgColor 			= new Color(80,40,180);
	private Color fgColor 			= Color.WHITE;
	
	private Font defFont			= ResourceManager.getFont("gnuolane.ttf");
	private Font gameFont			= new Font("Arial", Font.BOLD, 12);
	private Font viewFont			= new Font("Arial", Font.BOLD, 14);
	
	private JLabel name				= new JLabel("");
	private JLabel game 			= new JLabel("");
	private JLabel viewers 			= new JLabel("");
	private JLabel action 			= new JLabel("");
	
	public ChannelView(String viewname) {
		setName(viewname);
		setEnabled(false);
		setLayout(new GridBagLayout());

		viewers.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		name.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		game.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		action.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		
		action.setHorizontalAlignment(SwingConstants.RIGHT);
		
		name.setFont(defFont.deriveFont(Font.BOLD,20f));
		game.setFont(gameFont);
		viewers.setFont(viewFont);
		action.setFont(gameFont);
		action.setVisible(false);
		
		setBackground(bgColorOffline);
		
		action.setForeground(new Color(60,30,150));
		name.setForeground(fgColor);
		game.setForeground(fgColor);
		viewers.setForeground(fgColor.brighter());
		
		GridBagConstraints c = new GridBagConstraints();
		
		setMinimumSize(new Dimension(250,30));
		setPreferredSize(new Dimension(250,30));
		setMaximumSize(new Dimension(250,30));
		
		c.weightx = .1;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		add(viewers,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		add(name,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		add(game,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.gridx = 1;
		c.gridy = 1;
		add(action,c);
	}
	
	public void setChannelName(String name){
		ResourceManager.fixFontSize(this.name,name.toUpperCase());
	}
	
	public void setChannelGame(String game){
		ResourceManager.fixFontSize(this.game,game);
	}
	
	public void setChannelViewers(String viewers){
		ResourceManager.fixFontSize(this.viewers,(viewers.compareTo("0")==0 ? "" : viewers));
	}
	
	public void setChannelAction(String action){
		ResourceManager.fixFontSize(this.action,action);
	}
	
	public void setOnline(boolean online){
		if (online) {
			setBackground(bgColor);
			setMinimumSize(new Dimension(250,70));
			setPreferredSize(new Dimension(250,70));
			setMaximumSize(new Dimension(250,70));
			ResourceManager.fixFontSize(this.viewers,(viewers.getText().length() == 0 ? "0" : viewers.getText()));
		} else {
			setBackground(bgColorOffline);
			setMinimumSize(new Dimension(250,30));
			setPreferredSize(new Dimension(250,30));
			setMaximumSize(new Dimension(250,30));
		}
	}
	
	public void setHover(boolean hover){
		if (hover){
			setBackground(Color.WHITE);
			name.setForeground(bgColor);
			game.setForeground(bgColor);
			viewers.setForeground(bgColor);
			action.setForeground(bgColor);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		} else {
			setBackground(bgColor);
			name.setForeground(Color.WHITE);
			game.setForeground(Color.WHITE);
			viewers.setForeground(Color.WHITE);
			action.setForeground(Color.WHITE);		
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.game.getText().length() != 0) g.setColor(new Color(90,50,210).darker()); else g.setColor(Color.DARK_GRAY);
		g.drawLine(0,this.getHeight()-1,this.getWidth(),this.getHeight()-1);
		
		if (action.getText().compareTo("PLAYING")==0){
			g.setColor(name.getForeground());
			g.setFont(gameFont.deriveFont(Font.BOLD, 10));
			g.drawString("CURRENTLY WATCHING", this.getWidth()-140, 10);
		}
	}
	
}
