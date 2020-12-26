package com.bgi.edims.model;

public class EdiException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	public EdiException(String code) {
		this(code, null);
	}
	public EdiException(String code, String message) {
		super(message);
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	
	public static interface ERROR_CODE {
		public static final String SUCCESS="200";
		public static final String UNSUCCESS="400";
		public static final String UNKNOWN="10000";//未知错误	系统未知错误
		public static final String NETERROR="10001";//网络异常
		public static final String INVALID="10005";//参数错误
		public static final String NULL="10006";//必填字段为空
		public static final String TOOLONG="10008";//参数过长
		public static final String NOEXISTS="10009";//数据项不存在
		public static final String ILLEGAL="10010";//JSON对象格式错误
		public static final String LOGOUT="10086";//用户未登录
		public static final String NOPRO="10400";//用户无权限
	}
}
