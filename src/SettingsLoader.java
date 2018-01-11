import java.io.*;
import java.util.Properties;

public class SettingsLoader {
	private static final String settingsFile = "ReversiFX.properties";
	private static Properties prop = new Properties();

	public static void setSetting(String settingToSet, int value) {
		prop.setProperty(settingToSet, String.valueOf(value));
	}
	public static int getSetting(String settingToGet) {
		if (prop.getProperty(settingToGet)==null)
			return -1;
		return Integer.parseInt(prop.getProperty(settingToGet));
	}
	public static void loadSettings() throws IOException {
		InputStream input = null;
		try {
			input = new FileInputStream(settingsFile);
			prop.load(input);
		} catch (IOException e) {
			throw e;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void saveSettings() throws IOException{
		OutputStream output = null;
		try {
			output = new FileOutputStream(settingsFile);
			prop.store(output, null);
		} catch (IOException e) {
			throw e;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}
}
