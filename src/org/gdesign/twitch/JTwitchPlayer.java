package org.gdesign.twitch;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.gdesign.twitch.player.gui.controller.MainController;
import org.gdesign.twitch.player.gui.model.MainModel;
import org.gdesign.twitch.player.gui.view.MainView;
import org.gdesign.twitch.player.livestreamer.LivestreamerFactory;
import org.gdesign.utils.ResourceManager;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class JTwitchPlayer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.setProperty("awt.useSystemAAFontSettings", "true");
		System.setProperty("swing.aatext", "true");
		
		try {
			if (!(ResourceManager.initialize() && LivestreamerFactory.initialize()))
				throw new Exception("init failed.");
		} catch (Exception e) {
			LogManager.getLogger().error(e);
			JOptionPane.showMessageDialog(
					null,"Please create an issue at https://github.com/LostinAllThatCode/jtwitch/issues/ and attach your logfile from\n"+
							"./resources/logs\n"+e.toString() ,"Initialzing application data failed.", JOptionPane.OK_CANCEL_OPTION);
			System.exit(1);
		}

		if (checkDependencies()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new JTwitchPlayer();
				}
			});
		} else {
			LogManager.getLogger().error("Initialzing application data failed");
			JOptionPane.showMessageDialog(
							null,
							"Can't find vlc/livestreamer natives. Please configure livestreamer.properties.\n"
									+ "1) You need VideoLAN Player installed on your system. For Java(64Bit) you need VLC(64Bit) and vice versa.\n"
									+ "2) You need Livestreamer. See details in livestreamer.properties to set it up correctly.",
							"Libvlc/Livestreamer natives discovery",
							JOptionPane.OK_CANCEL_OPTION);
		}
	}

	public JTwitchPlayer() {
		String version = getClass().getPackage().getImplementationVersion();
		if (version == null) version = "[experimental]";
		JFrame frame = new JFrame("JTwitch Player " + version);
		frame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("jtwitchplayer.png")).getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, 1014, 480);
		frame.setMinimumSize(new Dimension(640, 480));
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				Dimension d = e.getComponent().getSize();
				if (d.width < e.getComponent().getMinimumSize().width) {
					d.width = e.getComponent().getMinimumSize().width;
				}
				if (d.height < e.getComponent().getMinimumSize().height) {
					d.height = e.getComponent().getMinimumSize().height;
				}
				e.getComponent().setSize(d);
			}
		});
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		MainView mainView = new MainView(frame);
		MainModel mainModel = new MainModel();
		
		new MainController(mainView, mainModel);
	}

	public static boolean checkDependencies() {
		if (new NativeDiscovery().discover() && LivestreamerFactory.discover()) {
			return true;
		}
		return false;
	}
}
