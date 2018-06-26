package com.ppmall.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 工具类
 * @author rex
 *
 */
public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * json字符串转对象
	 * @param jsonString
	 * @param valueType
	 * @return
	 */
	public static <T> T jsonStringToObject(String jsonString, Class<T> valueType) {
		try {
			return mapper.readValue(jsonString, valueType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 对象转json字符串
	 * @param object
	 * @return
	 */
	public static String objectToJsonString(Object object) {

		try {
			String returnString = mapper.writeValueAsString(object);
			return returnString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
