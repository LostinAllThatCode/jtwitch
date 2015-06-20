package org.gdesign.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.gdesign.utils.SystemInfo.OperatingSystem;

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
	
	@Override
	public String getProperty(String key) {
		String value = super.getProperty(key);
		if (SystemInfo.getOS().equals(OperatingSystem.UNIX)) value = parseWindowsPath(value);
		return value;
	}
	
	private static String parseWindowsPath(String path){
		return path.replace("\\", "/\\"); 
	}
	
	public static void checkConfig(Properties c1, Properties c2){				
	}
	
	public static boolean isRunningFromJar() {
		String className = Configuration.class.getName().replace('.', '/');
		String classJar = Configuration.class.getResource("/" + className + ".class").toString();
		return classJar.startsWith("jar:");
	}
}
