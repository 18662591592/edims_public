package com.bgi.edims.model;

import java.util.List;
import java.util.Set;

/**
 * 系统用户
 * @author yeyuanchun
 *
 */

public class EdiUser  extends EdiBaseBean{

	private String userId;//用户ID
	private String userName;//用户名
	private String md5Pwd;//用户密码MD5加密
	private String passWord;//
	private String realName;//真实姓名
	private EdiSupplier supplier;//供应商
	private String email;//邮箱地址
	private Integer isEmailOn;//开启邮箱发送
	private String credentialsSalt;//密码的盐
	private String supplierCode;//
	private EdiRole role;
	private String roleId;
	public interface STATUS{
		public static String NORMAL="1";
		public static String FORZEN="-1";
	}
	public interface TYPE{
		public static String NORMAL="1";
	}
	
	
//	public void init() {
//		if(null!=role) {
//			List<EdiMenu> menus= role.getMenus();
//			if(null!=menus&&menus.size()>0) {
//				for(EdiMenu ediMenu:menus) {
//				}
//			}
//		}
//	}
	public List<EdiMenu> getMenus(){
		if(null!=role) {
			return role.getMenus();
		}
		return null;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public EdiRole getRole() {
		return role;
	}
	public void setRole(EdiRole role) {
		this.role = role;
	}
	public String getCredentialsSalt() {
		return credentialsSalt;
	}
	public void setCredentialsSalt(String credentialsSalt) {
		this.credentialsSalt = credentialsSalt;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMd5Pwd() {
		return md5Pwd;
	}
	public void setMd5Pwd(String md5Pwd) {
		this.md5Pwd = md5Pwd;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public EdiSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(EdiSupplier supplier) {
		this.supplier = supplier;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getIsEmailOn() {
		return isEmailOn;
	}
	public void setIsEmailOn(Integer isEmailOn) {
		this.isEmailOn = isEmailOn;
	}
	
	
}
