package settingsFromFile;

import java.io.*;
import java.util.Properties;

/**
 * static class to load & save settings from a file
 */
public class SettingsLoader {
	private static final String settingsFile = "ReversiFX.properties";
	private static Properties prop = new Properties();

	/**
	 * set a single setting
	 * @param settingToSet the settings name to set
	 * @param value the settings value
	 */
	public static void setSetting(String settingToSet, int value) {
		prop.setProperty(settingToSet, String.valueOf(value));
	}

	/**
	 * get a single settings
	 * @param settingToGet the settings name to get
	 * @return value of that setting
	 */
	public static int getSetting(String settingToGet) {
		if (prop.getProperty(settingToGet)==null)
			return -1;
		return Integer.parseInt(prop.getProperty(settingToGet));
	}

	/**
	 * load the settings saved file
	 * @throws IOException (ie. if the file is not found)
	 */
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

	/**
	 * save all set settings to file
	 * @throws IOException if couldn't save file
	 */
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