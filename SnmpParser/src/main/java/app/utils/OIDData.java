package app.utils;

import java.util.HashMap;
import java.util.Map;

public class OIDData {

	Map<String, String> patternMap = new HashMap<>();

	public OIDData() {
		patternMap = new HashMap<>();
		patternMap.put("STRING", "(.+)\\s=\\sSTRING:\\s(.*)");
		patternMap.put("INTEGER", "(.+)\\s=\\sINTEGER:\\s(.*)");
		patternMap.put("Counter32", "(.+)\\s=\\sCounter32:\\s(.*)");
		patternMap.put("OID", "(.+)\\s=\\sOID:\\s(.*)");
		patternMap.put("Timeticks", "(.+)\\s=\\sTimeticks:\\s(.*)");
		patternMap.put("IpAddress", "(.+)\\s=\\sIpAddress:\\s(.*)");
	}

	public Map<String, String> getOIDData() {
		return patternMap;
	}

}