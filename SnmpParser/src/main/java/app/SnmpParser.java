package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import apputils.WriteReaderUtils;

public class SnmpParser {

	private static final Logger logger = Logger.getLogger(SnmpParser.class.getName());

	Properties prop;
	String oidDictionary;
	String path;

	public SnmpParser(String path) {
		this.path = path;
		try {
			oidDictionary = WriteReaderUtils.readFileAsString("oid_map.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void method1() {
		String dirGeneral = "data_snmp_general";

		File pathData = new File(path + dirGeneral);
		try {
			// List of all files and directories
			File filesList[] = pathData.listFiles();
			for (File file : filesList) {
				method2(file.getName(), file.getAbsolutePath(), dirGeneral);
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "error {0}...", e.getMessage());
			e.printStackTrace();
		}

	}

	private void method2(String fileName, String filePath, String dirGeneral) {
		fileName = fileName.replace("snmp_", "").replace(".txt", "");
		logger.log(Level.INFO, "parsing {0}...", fileName);

		Map<String, String> dataMap = new HashMap<>();
		dataMap.put("ip", fileName);

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				patternMapParser(dataMap, line);
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "paso1 {0}...", e.getMessage());
			e.printStackTrace();
		}

		String newFile = filePath.replace(dirGeneral, "data_snmp_ips");
		try (BufferedReader br = new BufferedReader(new FileReader(newFile))) {
			String line2;
			StringBuilder sb = new StringBuilder();
			while ((line2 = br.readLine()) != null) {
				Matcher matcher1 = Pattern.compile(".+\\s=\\sIpAddress:\\s(.+)", Pattern.CASE_INSENSITIVE)
						.matcher(line2);
				if (matcher1.find()) {
					sb.append(matcher1.group(1)).append(",");
				}
			}
			if (sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);

			dataMap.put("docsDevNmAccessIp", sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject(dataMap);
		String orgJsonData = jsonObject.toString();
		File outFile = new File(path + "data_json/json_" + fileName + ".txt");
		outFile.delete();
		WriteReaderUtils.WriteFile(outFile, orgJsonData);
	}

	public void patternMapParser(Map<String, String> datamap, String data) {
		String attr = "", value = "";
		Matcher matcher1 = Pattern.compile("(.+)\\s=\\s\\S+:\\s(.+)", Pattern.CASE_INSENSITIVE).matcher(data);
		Matcher matcher2 = Pattern.compile("(.+)\\s=", Pattern.CASE_INSENSITIVE).matcher(data);

		if (matcher1.find()) {
			attr = matcher1.group(1);
			value = matcher1.group(2).replace("\"", "");
			if (value == null)
				value = "";
		} else if (matcher2.find()) {
			attr = matcher2.group(1);
			value = "";
		}
		if (!attr.isEmpty()) {
			attr = this.getDictionary(attr);
			datamap.put(attr, value.trim());
		}
	}

	public String getDictionary(String oid) {
		if (this.oidDictionary != null) {
			String oid2 = oid.replace(".", "\\.");
			Matcher mchtr = Pattern.compile(oid2 + "\\|\\S+\\|(\\S+)", 2).matcher(this.oidDictionary);
			return mchtr.find() ? mchtr.group(1) : oid;
		} else {
			return oid;
		}
	}

}