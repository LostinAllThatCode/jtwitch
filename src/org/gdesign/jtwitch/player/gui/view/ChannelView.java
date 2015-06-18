package org.gdesign.jtwitch.player.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.gdesign.utils.FontResource;

public class ChannelView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3633213310390485830L;
	
	private Color bgColorOffline 	= Color.DARK_GRAY;
	private Color bgColor 			= new Color(80,40,180);
	private Color fgColor 			= Color.WHITE;
	
	private Font defFont			= FontResource.getFont("gnuolane.ttf");
	private Font gameFont			= new Font("Arial", Font.BOLD, 12);
	private Font viewFont			= new Font("Arial", Font.BOLD, 14);
	
	private JLabel name				= new JLabel("");
	private JLabel game 			= new JLabel("");
	private JLabel viewers 			= new JLabel("");
	private JLabel action 			= new JLabel("");
	
	public ChannelView() {
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
		this.name.setText(name.toUpperCase());
		this.setName(name);
		if (name != null) FontResource.fixFontSize(this.name);
		if (!isEnabled()) setEnabled(true);
	}
	
	public void setChannelGame(String game){
		this.game.setText(game);
		if (game != null) FontResource.fixFontSize(this.game);
	}
	
	public void setChannelViewers(String viewers){
		if (viewers.compareTo("0") == 0) this.viewers.setText(""); else this.viewers.setText(viewers);
		if (viewers != null)  FontResource.fixFontSize(this.viewers);
	}
	
	public void setChannelAction(String action){
		this.action.setText(action);
		if (action != null) FontResource.fixFontSize(this.action);
	}
	
	public void setOnline(boolean online){
		if (online) {
			setBackground(bgColor);
			setMinimumSize(new Dimension(250,70));
			setPreferredSize(new Dimension(250,70));
			setMaximumSize(new Dimension(250,70));
		} else {
			setBackground(bgColorOffline);
			setMinimumSize(new Dimension(250,30));
			setPreferredSize(new Dimension(250,30));
			setMaximumSize(new Dimension(250,30));
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.DARK_GRAY.brighter());
		g.drawLine(1,this.getHeight()-2,this.getWidth(),this.getHeight()-2);
	}
	
}
