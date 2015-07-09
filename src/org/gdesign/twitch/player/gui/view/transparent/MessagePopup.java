package org.gdesign.twitch.player.gui.view.transparent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.gdesign.utils.SystemInfo;
import org.gdesign.utils.SystemInfo.OperatingSystem;

public class MessagePopup extends TransparentFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	
	protected MessagePopup(JFrame frame,String message, Color fg, boolean timer) {
		super(frame);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setOffset(20, 42, JFrame.RIGHT_ALIGNMENT);		
		if (SystemInfo.getOS().equals(OperatingSystem.WIN)) {
			setOpacity(.8f);
		}
		add(textArea = new JTextArea(message));
		textArea.setFont(new Font("Lao UI",Font.BOLD,11));
		textArea.setMinimumSize(new Dimension(450,20));
		textArea.setPreferredSize(new Dimension(450,80));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBackground(Color.DARK_GRAY.darker());
		textArea.setForeground(fg);
		textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				setVisible(false);
				dispose();
			}
		});
		
		if (timer) {
			new Thread(new Runnable() {
				long timeout = 5000;
				long time = 0;
				@Override
				public void run() {
					while (time < timeout) {
						updateLocation();
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						time+=250;	
					}
					setVisible(false);
					dispose();
				}
			}).start();	
		}
		pack();
	}
	
	
}
