package org.gdesign.jtwitch.player.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Properties;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.gdesign.jtwitch.player.gui.controller.MainController;
import org.gdesign.jtwitch.player.gui.model.MainModel;
import org.gdesign.jtwitch.player.gui.view.MainView;
import org.gdesign.utils.Configuration;
import org.gdesign.utils.SystemInfo;
import org.json.simple.parser.ParseException;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class JTwitch extends Observable{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","true");
		System.setProperty("swing.aatext", "true");
		
		LogManager.getLogger().debug(SystemInfo.getOS());
		
		final Properties config = new Configuration("jtwitch.properties");
		
        if (checkVLCj()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	try {
						new JTwitch();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
        } else {
        	LogManager.getLogger().error("Can't find vlc natives.");
        }
	}
	
	public JTwitch() throws ParseException {
		JFrame frame = new JFrame("JTwitch Player (0.1.alpha)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 1280, 640);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		MainView mainView 	= new MainView(frame);
		MainModel mainModel	= new MainModel("its1z0");
		MainController controller = new MainController(mainView, mainModel);
		
		frame.add(mainView);
		
		controller.updateGUI(5000);

	}
	
	
	public static boolean checkVLCj(){
		if (new NativeDiscovery().discover()){
			LogManager.getLogger().debug("libvlcj vlc native path: "+RuntimeUtil.getLibVlcLibraryName());
			return true;
		}
		return false;
	}
}
