package org.gdesign.twitch.player.gui.view.streams;

import java.awt.Color;
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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.view.streams.layout.StreamViewLayout;
import org.gdesign.twitch.player.gui.view.streams.layout.utils.LayoutUtils;
import org.gdesign.utils.ResourceManager;

public class StreamView extends JPanel{
	
	private static final long serialVersionUID = -3633213310390485830L;
	
	private JLabel name 	= new JLabel("");
	private JLabel game 	= new JLabel("");
	private JLabel viewers 	= new JLabel("");
	private JLabel icon		= new JLabel("");		
	
	private StreamViewLayout layout;
	public boolean hover, playing, following;
	private BufferedImage logo;

	public StreamView(StreamViewLayout strategy) {
		layout = strategy;		
		doCustomLayout();
		
		setLayout(new GridBagLayout());
		setDoubleBuffered(true);
		setEnabled(false);
		setVisible(false);
		
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
	
	public void setLayoutStrategy(StreamViewLayout s){
		this.layout = s;
		doCustomLayout();
	}
	
	public void setStreamIcon(URL url){
		try {
			if (url == null) return;
			if (icon.getIcon() == null){
				logo = ResourceManager.getLogo(url);
				icon.setIcon(new ImageIcon(logo.getScaledInstance(icon.getPreferredSize().width, icon.getPreferredSize().height, Image.SCALE_SMOOTH)));
			} else {
				if (icon.getIcon().getIconHeight() != icon.getPreferredSize().height) {
					icon.setIcon(new ImageIcon(logo.getScaledInstance(icon.getPreferredSize().width, icon.getPreferredSize().height, Image.SCALE_SMOOTH)));
				}	
			}
		} catch (IOException e) {
			LogManager.getLogger().warn("Unable to get logo from "+ url+"\n"+e);
		}
	}
	
	public void setFollowing(boolean following) {
		this.following = following;
	}
	
	public boolean isFollowing(){
		return following;
	}
	
	public void setStreamName(String name){
		this.setName(name);
	}

	public void setStreamDisplayName(String displayName) {
		if (this.name.getText().compareTo(displayName) != 0) LayoutUtils.fixFontSize(this.name, displayName.toUpperCase());
	}

	public void setStreamGame(String game) {
		if (this.game.getText().compareTo(game) != 0) LayoutUtils.fixFontSize(this.game, game);	
	}

	public void setStreamViewers(String viewers) {
		if (this.viewers.getText().compareTo(viewers) != 0) LayoutUtils.fixFontSize(this.viewers,(viewers.compareTo("0") == 0 ? "" : viewers));
	}

	public void setStreamPlaying(boolean playing) {
		this.playing = playing;
		repaint();
	}

	public void setHover(boolean hover) {
		this.hover = hover;
		doCustomLayout();
	}
	
	public void doCustomLayout(){
		if (hover) {
			setBackground(layout.bg_color_hover);
			name.setForeground(layout.name_fg_color_hover);
			game.setForeground(layout.game_fg_color_hover);
			viewers.setForeground(layout.viewers_fg_color_hover);
		} else {
			setBackground(layout.bg_color);
			name.setForeground(layout.name_fg_color);
			game.setForeground(layout.game_fg_color);
			viewers.setForeground(layout.viewers_fg_color);
		}
		
		setMinimumSize(layout.dim);
		setPreferredSize(layout.dim);
		setMaximumSize(layout.dim);		
		
		setCursor(layout.cursor);
		
		icon.setPreferredSize(new Dimension(layout.dim.height,layout.dim.height));
		name.setPreferredSize(new Dimension(layout.dim.width-layout.dim.height,(int) (layout.dim.height*.5f)));
		game.setPreferredSize(new Dimension(layout.dim.width-layout.dim.height,(int) (layout.dim.height*.35f)));
		viewers.setPreferredSize(new Dimension(layout.dim.width-layout.dim.height,(int) (layout.dim.height*.35f)));

		name.setBorder(layout.name_border);
		name.setFont(layout.name_font);
		game.setBorder(layout.game_border);
		game.setFont(layout.game_font);		
		viewers.setBorder(layout.viewers_border);
		viewers.setFont(layout.viewers_font);			
		viewers.setHorizontalAlignment(SwingConstants.RIGHT);

		validate();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		if (playing && getBackground().equals(layout.bg_color)) {
			GradientPaint gradient = new GradientPaint(0, 20, getBackground().brighter(), this.getWidth(), 20, getBackground());
			g2d.setPaint(gradient);
			g2d.fillRect(0, 0, this.getWidth(), 70);
		}
		g2d.setColor(new Color(90, 50, 210).darker());
		if (!hover) g2d.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
	}

}
