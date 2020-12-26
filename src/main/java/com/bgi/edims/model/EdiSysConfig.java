package com.bgi.edims.model;

import java.util.Date;

/**
 * 系统配置信息
 * @author yeyuanchun
 *
 */
public class EdiSysConfig  extends EdiBaseBean{

	private String configKey;//系统配置key
	private String configValue;//系统配置值
	public EdiSysConfig(String configKey,String configValue,EdiUser ediUser) {
		this.configKey=configKey;
		this.configValue=configValue;
		this.setLastModifyTime(new Date());
		this.setLastModifyUser(ediUser==null?"anno":ediUser.getUserName());
	}
	public EdiSysConfig(String configKey,String configValue) {
		this.configKey=configKey;
		this.configValue=configValue;
	}
	public EdiSysConfig() {
	}
	public interface STATUS{
		public static String NORMAL="1";
		public static String FORZEN="-1";
	}
	public interface TYPE{
		public static String NORMAL="1";
	}
	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
	public String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	@Override
	public String toString() {
		return "EdiSysConfig [configKey=" + configKey + ", configValue=" + configValue + "]";
	}
	
	
}
