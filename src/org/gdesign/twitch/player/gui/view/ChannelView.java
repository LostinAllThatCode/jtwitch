package org.gdesign.twitch.player.gui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.view.layout.channel.ChannelLayoutStrategy;
import org.gdesign.utils.ResourceManager;

public class ChannelView extends JPanel {
	
	private static final long serialVersionUID = -3633213310390485830L;
	
	private JLabel name 	= new JLabel("");
	private JLabel game 	= new JLabel("");
	private JLabel viewers 	= new JLabel("");
	private JLabel icon		= new JLabel("");		
	
	private ChannelLayoutStrategy layout;
	private boolean online, hover, playing;

	public ChannelView(String viewname, ChannelLayoutStrategy strategy) {
		layout = strategy;		
		doCustomLayout();
		
		setDoubleBuffered(true);
		setName(viewname);
		setEnabled(false);
		setLayout(new GridBagLayout());
		
		online = false;
		hover = false;
		playing = false;
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 3;
		add(icon, c);

		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 0;
		add(viewers, c);
		
		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		add(name, c);
		
		c.gridwidth = 0;
		c.anchor = GridBagConstraints.SOUTH;	
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = .1f;
		add(game, c);

	}
	
	public ChannelView setLayoutStrategy(ChannelLayoutStrategy s){
		this.layout = s;
		doCustomLayout();
		return this;
	}
	
	public ChannelView setIcon(URL url){
		try {
			if (url == null) return this;
			if (icon.getIcon() == null){
				BufferedImage img = ImageIO.read(url);
				icon.setIcon(new ImageIcon(img.getScaledInstance(icon.getPreferredSize().width, icon.getPreferredSize().height, Image.SCALE_SMOOTH)));
				LogManager.getLogger().debug("Set logo for "+this.name.getText());
			} else {
				if (icon.getIcon().getIconHeight() != icon.getPreferredSize().height) {
					BufferedImage img = ImageIO.read(url);
					icon.setIcon(new ImageIcon(img.getScaledInstance(icon.getPreferredSize().width, icon.getPreferredSize().height, Image.SCALE_SMOOTH)));
					LogManager.getLogger().debug("Updated logo for "+this.name.getText());
				}	
			}
		} catch (IOException e) {
			LogManager.getLogger().warn("Unable to get logo from "+ url+"\n"+e);
		}
		return this;
	}

	public ChannelView setChannelName(String name) {
		if (this.name.getText().compareTo(name) != 0) ResourceManager.fixFontSize(this.name, name.toUpperCase());
		return this;
	}

	public ChannelView setChannelGame(String game) {
		if (this.game.getText().compareTo(game) != 0) ResourceManager.fixFontSize(this.game, game);
		return this;	
	}

	public ChannelView setChannelViewers(String viewers) {
		if (this.viewers.getText().compareTo(viewers) != 0) ResourceManager.fixFontSize(this.viewers,(viewers.compareTo("0") == 0 ? "" : viewers));
		return this;
	}

	public ChannelView setChannelAction(String action) {
		if (action.length() != 0) playing = true; else playing = false;
		return this;
	}

	public ChannelView setOnline(boolean online) {
		this.online = online;
		return this;
	}

	public ChannelView setHover(boolean hover) {
		this.hover = hover;
		doCustomLayout();
		repaint();
		return this;
	}
	
	public void doCustomLayout(){
		if (online) {
			if (hover) {
				setBackground(layout.getBackgroundColorHover());
				name.setForeground(layout.getNameFontColorHover());
				game.setForeground(layout.getGameFontColorHover());
				viewers.setForeground(layout.getViewerFontColorHover());
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else {
				name.setForeground(layout.getNameFontColor());
				game.setForeground(layout.getGameFontColor());
				viewers.setForeground(layout.getViewerFontColor());
				setBackground(layout.getBackgroundColorOnline());
			}
			
			setMinimumSize(layout.getOnlineDimension());
			setPreferredSize(layout.getOnlineDimension());
			setMaximumSize(layout.getOnlineDimension());		
			
			icon.setPreferredSize(new Dimension(layout.getOnlineDimension().height,layout.getOnlineDimension().height));
			name.setPreferredSize(new Dimension(layout.getOnlineDimension().width-layout.getOnlineDimension().height,(int) (layout.getOnlineDimension().height*.5f)));
			game.setPreferredSize(new Dimension(layout.getOnlineDimension().width-layout.getOnlineDimension().height,(int) (layout.getOnlineDimension().height*.35f)));
			viewers.setPreferredSize(new Dimension(layout.getOnlineDimension().width-layout.getOnlineDimension().height,(int) (layout.getOnlineDimension().height*.35f)));

			name.setBorder(BorderFactory.createEmptyBorder(0, 8, -4, 0));
			name.setFont(layout.getNameFont().deriveFont(layout.getNameSizeOnline()));
			game.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 8));
			game.setFont(layout.getGameFont());		
			game.setHorizontalAlignment(SwingConstants.RIGHT);
			viewers.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 8));
			viewers.setFont(layout.getViewerFont());			
			viewers.setHorizontalAlignment(SwingConstants.RIGHT);
			
		} else {
			setBackground(layout.getBackgroundColorOffline());
			setMinimumSize(layout.getOfflineDimension());
			setPreferredSize(layout.getOfflineDimension());
			setMaximumSize(layout.getOfflineDimension());		
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			icon.setPreferredSize(new Dimension(layout.getOfflineDimension().height,layout.getOfflineDimension().height));
			name.setPreferredSize(new Dimension(layout.getOfflineDimension().width-layout.getOfflineDimension().height,(int) (layout.getOfflineDimension().height*.9f)));
			game.setPreferredSize(new Dimension(layout.getOfflineDimension().width-layout.getOfflineDimension().height,(int) (layout.getOfflineDimension().height*.35f)));
			viewers.setPreferredSize(new Dimension(layout.getOfflineDimension().width-layout.getOfflineDimension().height,(int) (layout.getOfflineDimension().height*.35f)));
			
			name.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
			name.setFont(layout.getNameFont().deriveFont(layout.getNameSizeOffline()));
			name.setForeground(ResourceManager.changeAlpha(layout.getNameFontColor(),.2f));	
			
			game.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
			game.setFont(layout.getGameFont());
			game.setForeground(layout.getGameFontColor());
			game.setHorizontalAlignment(SwingConstants.RIGHT);
			
			viewers.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
			viewers.setFont(layout.getViewerFont());
			viewers.setForeground(layout.getViewerFontColor());
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		if (playing && getBackground().equals(layout.getBackgroundColorOnline())) {
			GradientPaint gradient = new GradientPaint(0, 20, getBackground().brighter(), this.getWidth(), 20, getBackground());
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, this.getWidth(), 70);
		}
		if (online) g2d.setColor(new Color(90, 50, 210).darker()); else g2d.setColor(Color.DARK_GRAY.darker());
		g2d.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
	}

}
