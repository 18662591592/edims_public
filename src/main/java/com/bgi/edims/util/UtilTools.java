package com.bgi.edims.util;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

public class UtilTools {
	private static Logger logger=LoggerFactory.getLogger(UtilTools.class);
	
	public static void writeAjaxResult(HttpServletRequest request, HttpServletResponse response, String json) {
		try {
			String callback = request.getParameter("callback");
			if (callback == null) {
				writeJson(response, json);
			} else {
				writeJavascript(response, callback, json);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public static void writeJson(HttpServletResponse response, String json) throws Exception {
		writeJson(response, json, "utf-8");
	}
	
	public static void writeJson(HttpServletResponse response, String json, String encoding) throws Exception {
		PrintWriter pw = null;
		try {
			response.setContentType("text/json;charset=" + encoding);
			pw = response.getWriter();
			if (logger.isDebugEnabled()) {
				logger.debug(json);
			}
			pw.write(json);
		} catch (Exception e) {
			throw e;
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	public static void writeJavascript(HttpServletResponse response, String callback, String json) throws Exception {
		PrintWriter pw = null;
		try {
			response.setContentType("text/javascript;charset=utf-8");
			pw = response.getWriter();
			if (logger.isDebugEnabled()) {
				logger.debug(json);
			}
			pw.write(callback + "(" + json + ");");
		} catch (Exception e) {
			throw e;
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	public static String jsonElement2String(JsonElement jsonElement) {
		if(null==jsonElement){
			return null;
		}
		String jsonString = jsonElement.toString();
		if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
			jsonString = jsonString.substring(1, jsonString.length() - 1);
		}
		return jsonString;
	}
	/**
	 * 数字校验
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 数字校验
	 * @param str
	 * @return
	 */
	 public static boolean isBigDecimal(String str) {  
	        java.util.regex.Matcher match =null;  
	        if(isNumeric(str)==true){  
	            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");  
	            match = pattern.matcher(str.trim());  
	        }else{  
	            if(str.trim().indexOf(".")==-1){  
	                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[+-]?[0-9]*");  
	                match = pattern.matcher(str.trim());  
	            }else{  
	                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^[+-]?[0-9]+(\\.\\d{1,100}){1}");  
	                match = pattern.matcher(str.trim());                  
	            }  
	        }  
	        return match.matches();  
	 }
	 
	 public static boolean isNotNull(String str){
			if(str != null && !str.trim().equals("")){
				return true;
			}
			return false;
		}
	 
	 public static String replaceMallPicHost(String picUrl){
		 String url = "";
		 if(picUrl == null){
			 return url;
		 }
		 boolean isAbsolutePath = picUrl.startsWith("http://");
		 if (isAbsolutePath) {
			 url = picUrl.replaceFirst("http://.+?(?=/pic/)", "");
		 } else {
			if (picUrl.startsWith("/")) {
				url = picUrl;
			} else {
				url = "/" + picUrl;
			}
		}
		return url;
	}
	 
	 public static boolean isEmail(String string) {
	        if (string == null)
	            return false;
	        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	        Pattern p;
	        Matcher m;
	        p = Pattern.compile(regEx1);
	        m = p.matcher(string);
	        if (m.matches())
	            return true;
	        else
	            return false;
	    }
	
}
