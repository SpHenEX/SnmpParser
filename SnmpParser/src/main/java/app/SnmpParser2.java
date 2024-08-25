package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import apputils.WriteReaderUtils;

public class SnmpParser2 {

	private static final Logger logger = Logger.getLogger(SnmpParser.class.getName());

	String path;

	public SnmpParser2(String path) {
		this.path = path;
	}

	public void method4() {
		File pathData = new File(path + "dsm_ip");

		try {
			// List of all files and directories
			File filesList[] = pathData.listFiles();
			for (File file : filesList) {

				method5(path, file.getName(), file.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Map<String, String> method5(String path, String fileName, String filePath) {
		Map<String, String> dataMap = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			dataMap.put("ip", fileName);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				Matcher matcher1 = Pattern.compile(".+\\s=\\sIpAddress:\\s(.+)", Pattern.CASE_INSENSITIVE)
						.matcher(line);
				if (matcher1.find()) {
					sb.append(matcher1.group(1)).append(",");
				}
			}

			String str = sb.toString();
			if (str.endsWith(","))
				str = str.substring(0, str.length() - 1);

			dataMap.put("docsDevNmAccessIp", str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataMap;
	}

}