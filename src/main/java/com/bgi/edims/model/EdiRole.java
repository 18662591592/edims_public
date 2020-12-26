package com.bgi.edims.model;

import java.util.List;

/**
 * 
 * @author yeyuanchun
 *
 */
public class EdiRole  extends EdiBaseBean{

	private String userId;//用户ID
	private String roleId;//角色ID
	private String roleName;//角色名称
	private List<EdiMenu> menus;//菜单权限
	public interface STATUS{
		public static String NORMAL="1";
		public static String FORZEN="-1";
	}
	public interface TYPE{
		public static String NORMAL="1";
	}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<EdiMenu> getMenus() {
		return menus;
	}
	public void setMenus(List<EdiMenu> menus) {
		this.menus = menus;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	} 
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "EdiRole [userId=" + userId + ", roleId=" + roleId + ", roleName=" + roleName + ", menus=" + menus
				+ ", getStartIndex()=" + getStartIndex() + ", getRows()=" + getRows() + ", getPage()=" + getPage()
				+ ", getTotalRecord()=" + getTotalRecord() + ", getStatus()=" + getStatus() + ", getType()=" + getType()
				+ ", getLastModifyTime()=" + getLastModifyTime() + ", getLastModifyUser()=" + getLastModifyUser()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
}
