package org.gdesign.jtwitch.player.gui.templates;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.gdesign.utils.FontResource;

public class Channel extends JPanel implements Observer {

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
	private JLabel status 			= new JLabel("");
	
	public Channel(String channelName) {
		setName(channelName);
		
		setView();
		setLayout(new GridBagLayout());
		
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
		add(status,c);
		
	}
	
	private void setView(){	
		name.setText(getName().toUpperCase());

		viewers.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		name.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		game.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		status.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		
		status.setHorizontalAlignment(SwingConstants.RIGHT);
		
		name.setFont(defFont.deriveFont(Font.BOLD,20f));
		game.setFont(gameFont);
		viewers.setFont(viewFont);
		status.setFont(gameFont);
		
		fixFontSize(name);
		
		setBackground(bgColorOffline);
		
		status.setForeground(new Color(60,30,150));
		name.setForeground(fgColor);
		game.setForeground(fgColor);
		viewers.setForeground(fgColor.brighter());
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				e.getComponent().setBackground(e.getComponent().getBackground().brighter());
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseEntered(e);
				e.getComponent().setBackground(e.getComponent().getBackground().darker());
			}
		});	
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.DARK_GRAY.brighter());
		g.drawLine(1,this.getHeight()-2,this.getWidth(),this.getHeight()-2);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		String[] cmd = arg.toString().split("§");
		if (cmd[0].compareTo(getName()) == 0){
			if (cmd[1].compareTo("game") == 0) {
				game.setText(cmd[2]);
				fixFontSize(game);
			}
			if (cmd[1].compareTo("viewers") == 0) viewers.setText(cmd[2]);
			if (cmd[1].compareTo("online") == 0) {
				setBackground(bgColor);
				setMinimumSize(new Dimension(250,70));
				setPreferredSize(new Dimension(250,70));
				setMaximumSize(new Dimension(250,70));
			}
			if (cmd[1].compareTo("offline") == 0) {
				setBackground(bgColorOffline);
				setMinimumSize(new Dimension(250,30));
				setPreferredSize(new Dimension(250,30));
				setMaximumSize(new Dimension(250,30));
			}
			if (cmd[1].compareTo("start") == 0) {
				status.setText("starting");
			}
			if (cmd[1].compareTo("stopped") == 0) {
				status.setText("");
			}
			if (cmd[1].compareTo("play") == 0) {
				status.setText("watching");
			}
		}
		revalidate();
	}	
	
	private void fixFontSize(JLabel label){
		Font labelFont = label.getFont();
		String labelText = label.getText();

		int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = label.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = label.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		// Set the label's font size to the newly determined size.
		label.setFont(label.getFont().deriveFont(fontSizeToUse));
	}
	
}
