package com.ppmall.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtil {
	private static String REQUEST_METHOD_GET = "GET";

	private static String REQUEST_METHOD_POST = "POST";

	private static String URLCODE = "UTF-8";

	private static int CONNECT_TIME_OUT = 15000;

	private static int READ_TIME_OUT = 15000;
	
	private static HttpURLConnection connection = null;
	//private static HttpsURLConnection sconnection = null;
	private static InputStream is = null;
	private static BufferedReader br = null;

	public static String httpPost(String urlStr) {
		
		String result = null;
		try {
			// 创建远程url连接对象
			URL url = new URL(urlStr);
			// 通过远程url连接对象打开一个连接，强转成httpURLConnection类
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod(REQUEST_METHOD_POST);
			// 设置连接主机服务器的超时时间：15000毫秒
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(READ_TIME_OUT);
			// 发送请求
			connection.connect();
			// 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				// 封装输入流is，并指定字符集
				br = new BufferedReader(new InputStreamReader(is, URLCODE));
				// 存放数据
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			connection.disconnect();// 关闭远程连接
		}

		return result;
	}

	public static String httpGet(String urlStr,String param) {
		String result = null;
		OutputStream os = null;
		try {
			// 创建远程url连接对象
			URL url = new URL(urlStr);
			// 通过远程url连接对象打开一个连接，强转成httpURLConnection类
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod(REQUEST_METHOD_GET);
			// 设置连接主机服务器的超时时间：15000毫秒
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(READ_TIME_OUT);
			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
			// 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				// 封装输入流is，并指定字符集
				br = new BufferedReader(new InputStreamReader(is, URLCODE));
				// 存放数据
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			connection.disconnect();// 关闭远程连接
		}

		return result;
	}

	public static String httpsPost(String urlStr,String param) {
		String result = null;
		OutputStream os = null;
		try {
			// 创建远程url连接对象
			URL url = new URL(urlStr);
			// 通过远程url连接对象打开一个连接，强转成httpURLConnection类
			connection = (HttpsURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod(REQUEST_METHOD_POST);
			// 设置连接主机服务器的超时时间：15000毫秒
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(READ_TIME_OUT);
			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
			// 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				// 封装输入流is，并指定字符集
				br = new BufferedReader(new InputStreamReader(is, URLCODE));
				// 存放数据
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			connection.disconnect();// 关闭远程连接
		}

		return result;
	}

	public static String httpsGet(String urlStr) {
		String result = null;
		try {
			// 创建远程url连接对象
			URL url = new URL(urlStr);
			// 通过远程url连接对象打开一个连接，强转成httpURLConnection类
			connection = (HttpsURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod(REQUEST_METHOD_GET);
			// 设置连接主机服务器的超时时间：15000毫秒
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(READ_TIME_OUT);
			// 发送请求
			connection.connect();
			// 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();
				// 封装输入流is，并指定字符集
				br = new BufferedReader(new InputStreamReader(is, URLCODE));
				// 存放数据
				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			connection.disconnect();// 关闭远程连接
		}

		return result;
	}
}
