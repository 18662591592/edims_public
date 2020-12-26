package com.bgi.edims.model;
/**
 * 模块对象
 * @author yeyuanchun
 *
 */
public class EdiMenu  extends EdiBaseBean{

	private String menuId;//模块ID
	private String roleId;//角色ID
	private String menuName;//模块名称
	private Boolean query;//是否拥有查询权限
	private Boolean create;//是否拥有新增权限
	private Boolean update;//是否拥有更新权限
	private Boolean delete;//是否拥有删除权限
	private Boolean upload;//是否拥有上传权限
	private Boolean download;//是否拥有下载权限
	private String menuDesc;//模块描述
	private String icon;
	private String url;
	private String group;
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public interface STATUS{
		public static String NORMAL="1";
		public static String FORZEN="-1";
	}
	public interface TYPE{
		public static String NORMAL="1";
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMenuDesc() {
		return menuDesc;
	}
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}
	public Boolean getQuery() {
		return query;
	}
	public void setQuery(Boolean query) {
		this.query = query;
	}
	public Boolean getCreate() {
		return create;
	}
	public void setCreate(Boolean create) {
		this.create = create;
	}
	public Boolean getUpdate() {
		return update;
	}
	public void setUpdate(Boolean update) {
		this.update = update;
	}
	public Boolean getDelete() {
		return delete;
	}
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	public Boolean getUpload() {
		return upload;
	}
	public void setUpload(Boolean upload) {
		this.upload = upload;
	}
	public Boolean getDownload() {
		return download;
	}
	public void setDownload(Boolean download) {
		this.download = download;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	@Override
	public String toString() {
		return "EdiMenu [menuId=" + menuId + ", roleId=" + roleId + ", menuName=" + menuName + ", query=" + query
				+ ", create=" + create + ", update=" + update + ", delete=" + delete + ", upload=" + upload
				+ ", download=" + download + ", menuDesc=" + menuDesc + ", icon=" + icon + ", url=" + url + ", group="
				+ group + "]";
	}
	
	
}
