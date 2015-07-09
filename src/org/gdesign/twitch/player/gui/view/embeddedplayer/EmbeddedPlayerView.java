package org.gdesign.twitch.player.gui.view.embeddedplayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.gdesign.twitch.player.gui.view.embeddedplayer.EmbeddedPlayerControlView.Control;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.utils.SystemInfo;
import org.gdesign.utils.SystemInfo.OperatingSystem;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.x.XFullScreenStrategy;

public class EmbeddedPlayerView extends JPanel {

	private EmbeddedMediaPlayerComponent embeddedPlayer;
	private static final long serialVersionUID = 1L;
	private int maxVolume, defVolume;
	private JPanel controls;
	private JPopupMenu popupQuali;

	public EmbeddedPlayerView(final JFrame j) {
		setLayout(new BorderLayout());

		maxVolume = 200;
		defVolume = maxVolume / 4;

		embeddedPlayer = new EmbeddedMediaPlayerComponent() {
			private static final long serialVersionUID = 1L;

			@Override
			protected FullScreenStrategy onGetFullScreenStrategy() {
				if (SystemInfo.getOS().equals(OperatingSystem.WIN))
					return new Win32FullScreenStrategy(j);
				else if (SystemInfo.getOS().equals(OperatingSystem.UNIX))
					return new XFullScreenStrategy(j);
				else
					return null;
			};

			@Override
			public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
				if (mediaPlayer.getVolume() != defVolume) {
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
					setVolume(defVolume);
				}
				
			}
		};

		controls = new JPanel();
		controls.setBackground(Color.DARK_GRAY);
		controls.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		EmbeddedPlayerControlView playstop = new EmbeddedPlayerControlView(Control.PLAY_STOP);
		EmbeddedPlayerControlView fullscreen = new EmbeddedPlayerControlView(Control.FULLSCREEN, true);
		EmbeddedPlayerControlView vol = new EmbeddedPlayerControlView(Control.VOLUME, true);
		EmbeddedPlayerControlView status = new EmbeddedPlayerControlView(Control.STATUS);
		EmbeddedPlayerControlView quality = new EmbeddedPlayerControlView(Control.QUALITY, true);

		popupQuali = new JPopupMenu();
		popupQuali.setLayout(new BoxLayout(popupQuali, BoxLayout.Y_AXIS));
		popupQuali.setBorder(BorderFactory.createEmptyBorder(0, -2, 0, 0));
		
		//new Color(80, 40, 180)
		
		JMenuItem source = new JMenuItem("Source");
		source.setFont(new Font("Arial", Font.PLAIN, 10));
		source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		popupQuali.add(source);

		JMenuItem high = new JMenuItem("High");
		high.setFont(new Font("Arial", Font.PLAIN, 10));
		high.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		popupQuali.add(high);

		JMenuItem medium = new JMenuItem("Medium");
		medium.setFont(new Font("Arial", Font.PLAIN, 10));
		medium.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		popupQuali.add(medium);

		JMenuItem low = new JMenuItem("Low");
		low.setFont(new Font("Arial", Font.PLAIN, 10));
		low.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		popupQuali.add(low);

		JMenuItem audio = new JMenuItem("Audio");
		audio.setFont(new Font("Arial", Font.PLAIN, 10));
		audio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		popupQuali.add(audio);

		add(popupQuali);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		controls.add(playstop, c);
		c.weightx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		c.gridy = 0;
		controls.add(status, c);
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.gridy = 0;
		controls.add(vol, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 3;
		c.gridy = 0;
		controls.add(quality, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 4;
		c.gridy = 0;
		controls.add(fullscreen, c);

		this.add(embeddedPlayer, BorderLayout.CENTER);
		this.add(controls, BorderLayout.PAGE_END);
	}

	private EmbeddedPlayerControlView getControl(String name) {
		for (Component c : controls.getComponents()) {
			if (c.getName() != null) {
				if (name.compareTo(c.getName()) == 0)
					return (EmbeddedPlayerControlView) c;
			}
		}
		return null;
	}

	public void setControlValue(String control, Object value) {
		getControl(control).setValue(value);
	}

	public void playMedia(String mrl, String mediaOptions) {
		embeddedPlayer.getMediaPlayer().playMedia(mrl, mediaOptions);
		getControl("PLAY_STOP").setActive(true);
	}

	public boolean isPlaying() {
		return embeddedPlayer.getMediaPlayer().isPlaying();
	}

	public void stopMedia() {
		embeddedPlayer.getMediaPlayer().stop();
		getControl("PLAY_STOP").setActive(false);
	}

	public void setVolume(int value) {
		setControlValue("VOLUME", (value <= maxVolume + 10 ? value : maxVolume));
		embeddedPlayer.getMediaPlayer().setVolume(defVolume = (value <= maxVolume + 10 ? value : maxVolume));
	}

	public int getVolume() {
		return embeddedPlayer.getMediaPlayer().getVolume();
	}

	public void toggleFullscreen() {
		embeddedPlayer.getMediaPlayer().toggleFullScreen();
	}

	public boolean isFullscreen() {
		return embeddedPlayer.getMediaPlayer().isFullScreen();
	}

	public void toggleQualityPopup() {
		setQuality(LivestreamerFactory.getDefaultQuality());
		Point p = getControl("QUALITY").getLocationOnScreen();
		p.translate(-21, -85);
		popupQuali.setLocation(p);
		popupQuali.setVisible(!popupQuali.isVisible());
	}

	public void setQuality(String quality) {
		for (Component c : popupQuali.getComponents()) {
			JMenuItem i = (JMenuItem) c;
			i.setBackground(Color.DARK_GRAY.darker());
			
			if (i.getText().toLowerCase().compareTo(quality.toLowerCase()) == 0) {
				i.setFont(i.getFont().deriveFont(Font.ITALIC+Font.BOLD,10));
				i.setForeground(Color.WHITE);
			} else {
				i.setFont(i.getFont().deriveFont(Font.PLAIN));
				i.setForeground(Color.WHITE.darker());
			}
				
		}
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		for (Component c : controls.getComponents()) {
			c.addMouseListener(l);
			c.addMouseMotionListener((MouseMotionListener) l);
		}
		for (Component d : popupQuali.getComponents())
			d.addMouseListener(l);
	}

}
