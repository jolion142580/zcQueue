package com.yiko.common.utils.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	/**
	 * 迭代读取多层json数据
	 * @param jsonStr
	 * @return
	 */
		public static Map<String, Object> parseJSON2Map(String jsonStr) {

			if (jsonStr != null && jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject json = JSONObject.fromObject(jsonStr);

				for (Object k : json.keySet()) {
					Object v = json.get(k);
					// 如果内层还是数组的话，继续解析
					if (v instanceof JSONArray) {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Iterator it = ((JSONArray) v).iterator();
						while (it.hasNext()) {
							JSONObject json2 = (JSONObject) it.next();
							list.add(parseJSON2Map(json2.toString()));
						}
						map.put(k.toString(), list);
					} else {
						Map<String, Object> m = parseJSON2Map(v.toString());
						if (m == null)
							map.put(k.toString(), v);
						else
							map.put(k.toString(), m);
					}
				}
				return map;

			} else {
				return null;
			}

		}
}
