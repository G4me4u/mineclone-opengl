package minecraft.util;

import java.io.File;
import java.util.Locale;

public class LibUtil {

	private static final String NATIVES_PATH = "./lib/natives";
	private static final String WINDOWS_NATIVES_X64_PATH = NATIVES_PATH + "/windows/x64";
	private static final String WINDOWS_NATIVES_X86_PATH = NATIVES_PATH + "/windows/x86";
	private static final String LINUX_NATIVES_ARM32_PATH = NATIVES_PATH + "/linux_arm32";

	public static void loadNatives() {
		String libraryPath = System.getProperty("org.lwjgl.librarypath");
		String nativePath = new File(getNativesPath()).getAbsolutePath();
		System.setProperty("org.lwjgl.librarypath", nativePath + File.pathSeparator + libraryPath);
	}
	
	public static String getNativesPath() {
		String osname = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if (osname.indexOf("win") >= 0) {
			String jvmVersion = System.getProperty("sun.arch.data.model");
			if (jvmVersion.indexOf("64") >= 0)
				return WINDOWS_NATIVES_X64_PATH;
			return WINDOWS_NATIVES_X86_PATH;
		}
		if (osname.indexOf("nux") >= 0)
			return LINUX_NATIVES_ARM32_PATH;
		
		throw new UnknownOSException("Unknown OS: " + osname);
	}
}
