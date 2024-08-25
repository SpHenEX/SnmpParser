package apputils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesLoader {

	public PropertiesLoader() {
		// default constructor
	}

	private static final Logger logger = Logger.getLogger(PropertiesLoader.class.getName());

	public Properties getProperties(String file) {
		try {
			String jarPath = PropertiesLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			File jarFile = new File(jarPath);
			String filePath = jarFile.getParent() + "/" + file;

			FileInputStream fis = new FileInputStream(filePath);
			Properties props = new Properties();
			props.load(fis);
			fis.close();
			return props;
		} catch (IOException | URISyntaxException e) {
			logger.log(Level.INFO, "error {0}", e.getMessage());
			return getPropertiesLocal();
		}
	}

	public Properties getPropertiesLocal() {
		Properties props = null;
		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream("config.properties");
			props = new Properties();
			props.load(stream);
			stream.close();
			return props;
		} catch (Exception e) {
			logger.log(Level.WARNING, "error properties file not found");
			e.printStackTrace();
		}
		return props;
	}
}
