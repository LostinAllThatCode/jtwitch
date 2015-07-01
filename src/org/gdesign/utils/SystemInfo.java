package org.gdesign.utils;

public class SystemInfo {

	public static enum OperatingSystem {
		UNIX, MAC, WIN, UNKNOWN
	}

	public static OperatingSystem getOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows"))
			return OperatingSystem.WIN;
		else if (os.contains("unix") || os.contains("linux"))
			return OperatingSystem.UNIX;
		else if (os.contains("mac"))
			return OperatingSystem.UNIX;
		else
			return OperatingSystem.UNKNOWN;
	}
}
