package org.gdesign.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Configuration extends Properties{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2741726975760709388L;

	public Configuration(String propertyFile) {
		try {
			if (isRunningFromJar()){
				File f = new File("./"+propertyFile);
				if (!f.exists()) JarExport.ExportResource("/"+propertyFile);
				load(new FileInputStream(f));
			} else {
				load(ClassLoader.getSystemResource(propertyFile).openStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	public static boolean isRunningFromJar() {
		String className = Configuration.class.getName().replace('.', '/');
		String classJar = Configuration.class.getResource("/" + className + ".class").toString();
		return classJar.startsWith("jar:");
	}
}
