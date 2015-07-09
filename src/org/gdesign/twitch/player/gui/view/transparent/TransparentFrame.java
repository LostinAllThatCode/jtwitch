package org.gdesign.twitch.player.gui.view.transparent;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.gdesign.utils.SystemInfo;
import org.gdesign.utils.SystemInfo.OperatingSystem;

public class TransparentFrame extends JFrame {

	private static final long serialVersionUID = -5586641144351526681L;
	public JFrame parent;
	
	private int offsetX, offsetY;
	private float alignment;
	
	public TransparentFrame(JFrame frame) {
	    this.parent = frame;
	    setType(JFrame.Type.UTILITY);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setUndecorated(true);
	    setFocusableWindowState(false);
	    
	    setVisible(true);
	    setLocationRelativeTo(frame);
	    
	    parent.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				super.componentMoved(e);
				updateLocation();
			}
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				updateLocation();
			}
		});
		
		new Thread(new Runnable() {			
			@Override
			public void run() {
				while (isVisible()) {
					if (parent.isFocused() && isVisible()) toFront();
					Point p = getLocationOnScreen();
					if (MouseInfo.getPointerInfo().getLocation().x >= p.x && MouseInfo.getPointerInfo().getLocation().x <= p.x + getWidth() 
							&& MouseInfo.getPointerInfo().getLocation().y >= p.y && MouseInfo.getPointerInfo().getLocation().y <= p.y + getHeight()) {
						setHoverOpacity(true);
					} else setHoverOpacity(false);
					pack();
					if (getSize().height > parent.getContentPane().getHeight()-36) 
						setSize(Math.min(250, parent.getWidth()), parent.getContentPane().getHeight()-36);
					validate();
					repaint();
					try {
						Thread.sleep(120);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
			}
		}).start();
	}
	
	public void setOffset(int x, int y, float alignment) {
		this.offsetX = x;
		this.offsetY = y;
		this.alignment = alignment;
	}
	
	public void setHoverOpacity(boolean hover){
		if (SystemInfo.getOS().equals(OperatingSystem.WIN)) {
			if (hover) {
				if (getOpacity() != 1f) setOpacity(1f);
			} else {
				if (getOpacity() != .45f) setOpacity(.45f);
			}
		}
	}
	
	public void updateLocation(){
		if (alignment == JComponent.LEFT_ALIGNMENT) {
			Point p = parent.getLocationOnScreen();
			p.translate(offsetX, offsetY);
			setLocation(p);
		} else if (alignment == JComponent.RIGHT_ALIGNMENT){
			Point p = parent.getLocationOnScreen();
			p.translate(parent.getWidth()-getWidth()-offsetX, offsetY);
			setLocation(p);
		} else {
			setLocationRelativeTo(parent);
		}
		pack();
		if (getSize().height > parent.getContentPane().getHeight()-36) 
			setSize(Math.min(250, parent.getWidth()), parent.getContentPane().getHeight()-36);
	}
	
}