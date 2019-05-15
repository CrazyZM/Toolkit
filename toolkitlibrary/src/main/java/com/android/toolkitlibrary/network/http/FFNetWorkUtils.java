package com.android.toolkitlibrary.network.http;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



public class FFNetWorkUtils {
	/**
	 * 获取get方法字符
	 * @param paramArray
	 * @return
	 */
	public static String getGetString(Object... paramArray){
		StringBuilder params = new StringBuilder("?");
		if (paramArray != null && paramArray.length != 0) {
			int max = paramArray.length;
			for (int i = 0; i < max; i++) {
				Object str = paramArray[++i] == null ? "" : paramArray[i];
				try {
					params.append(paramArray[i - 1] + "="
							+ URLEncoder.encode(str.toString(), "utf-8") + "&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return params.toString().substring(0, params.length() - 1);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static int getImageType(File file) {
		if (!file.exists()) {
			return 0;
		}
		InputStream is = null;
		int fileType = 0;
		try {
			is = new FileInputStream(file);
			byte[] buffer = new byte[2];
			String fileCode = "";
			if (is.read(buffer) != -1) {
				for (int i = 0; i < buffer.length; i++) {
					fileCode += Integer.toString((buffer[i] & 0xFF));
				}
				fileType = Integer.parseInt(fileCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return fileType;
	}
}
