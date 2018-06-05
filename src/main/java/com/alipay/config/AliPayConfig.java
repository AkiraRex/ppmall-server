package com.alipay.config;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayClient;

public class AliPayConfig {
	private static Logger logger = LoggerFactory.getLogger(AliPayConfig.class);
	private static Properties alipayConfigProperties = null;
	private static AlipayClient alipayClient = null;
	
	public static AlipayClient getAlipayClient() {
		return alipayClient;
	}
	
	static {
		reloadConfig();
	}
	
	public synchronized static void reloadConfig() {
		if (alipayConfigProperties == null) {
			alipayConfigProperties = new Properties();
		} else {
			alipayConfigProperties.clear();
		}
		loadProperties(alipayConfigProperties, "alipayConfig.properties");
	}

	public static String getConfigValue(String key) {
		if (alipayConfigProperties == null) {
			return null;
		}
		return alipayConfigProperties.getProperty(key);
	}
	
	public static void loadProperties(Properties properties, String fileName) {
		if (properties == null || fileName == null) {
			return;
		}
		try {
			String root = AliPayConfig.class.getResource("/").getPath();

			root = decodeFilePath(root);
			String file = root + fileName;
			logger.debug("loadProperties()-- fileName=[" + file + "]");
			InputStream is = AliPayConfig.class.getResourceAsStream("/" + fileName);
			properties.load(is);
			is.close();
		} catch (Exception e) {
			logger.error("loadProperties()[" + fileName + "]:" + e);
		}
	}

	public static String decodeFilePath(String root) {
		try {
			root = URLDecoder.decode(root, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		if (root != null && root.toLowerCase().indexOf("http:") == -1 && root.indexOf("%20") != -1) {
			root = root.replace("%20", " ");
		}
		return root;
	}
}
