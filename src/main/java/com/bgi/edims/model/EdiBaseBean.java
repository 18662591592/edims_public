package com.bgi.edims.model;

import java.util.Date;
/**
 * EDI基础对象
 * @author yeyuanchun
 *
 */
public class EdiBaseBean {
	private String status;//状态
	private String type;//类型
	private Date lastModifyTime;//最后修改时间
	private String lastModifyUser;//最后修改用户
	private Integer rows;//每页条数
	private Integer page = 1;//页码
	private Integer startIndex;
	private Integer totalRecord;//总条数
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		if(null!=rows&&null!=page) {
			startIndex=(page-1)*rows;
		}
		this.rows = rows;
	}
	public Integer getPage() {
		
		return page;
	}
	public void setPage(Integer page) {
		if(null!=rows&&null!=page) {
			startIndex=(page-1)*rows;
		}
		this.page = page;
	}
	public Integer getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getLastModifyUser() {
		return lastModifyUser;
	}
	public void setLastModifyUser(String lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}
	
}
