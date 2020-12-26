package com.bgi.edims.model;

import java.util.Date;
/**
 * 订单出货信息
 * @author yeyuanchun
 *
 */
public class EdiAsn extends EdiBaseBean{

	private String asnId;//出货信息编码
	private EdiSupplier supplier;//供应商
	private String supplierCode;//供应商编码
	private String fileName;//文件名称
	private String fileUrl;//文件路径
	private String userId;//
	private EdiUser uploadUser;//上传用户
	private Date uploadTime;//上传时间
	private Integer downloadTimes;//下载次数
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
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getAsnId() {
		return asnId;
	}
	public void setAsnId(String asnId) {
		this.asnId = asnId;
	}
	public EdiSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(EdiSupplier supplier) {
		this.supplier = supplier;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public EdiUser getUploadUser() {
		return uploadUser;
	}
	public void setUploadUser(EdiUser uploadUser) {
		this.uploadUser = uploadUser;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Integer getDownloadTimes() {
		return downloadTimes;
	}
	public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
	@Override
	public String toString() {
		return "EdiAsn [asnId=" + asnId + ", supplier=" + supplier + ", supplierCode=" + supplierCode + ", fileName="
				+ fileName + ", fileUrl=" + fileUrl + ", userId=" + userId + ", uploadUser=" + uploadUser
				+ ", uploadTime=" + uploadTime + ", downloadTimes=" + downloadTimes + ", getStartIndex()="
				+ getStartIndex() + ", getRows()=" + getRows() + ", getPage()=" + getPage() + ", getTotalRecord()="
				+ getTotalRecord() + ", getStatus()=" + getStatus() + ", getType()=" + getType()
				+ ", getLastModifyTime()=" + getLastModifyTime() + ", getLastModifyUser()=" + getLastModifyUser()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
	
}
