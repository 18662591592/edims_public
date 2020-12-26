package com.bgi.edims.model;

public class EdiOrderRequst extends EdiOrder {
	private String uploadStartTime;
	private String uploadEndTime;
	private String downloadStartTime;
	private String downloadEndTime;
	private String isDownloaded;
	public String getUploadStartTime() {
		return uploadStartTime;
	}
	public void setUploadStartTime(String uploadStartTime) {
		this.uploadStartTime = uploadStartTime;
	}
	public String getUploadEndTime() {
		return uploadEndTime;
	}
	public void setUploadEndTime(String uploadEndTime) {
		this.uploadEndTime = uploadEndTime;
	}
	public String getDownloadStartTime() {
		return downloadStartTime;
	}
	public void setDownloadStartTime(String downloadStartTime) {
		this.downloadStartTime = downloadStartTime;
	}
	public String getDownloadEndTime() {
		return downloadEndTime;
	}
	public void setDownloadEndTime(String downloadEndTime) {
		this.downloadEndTime = downloadEndTime;
	}
	public String getIsDownloaded() {
		return isDownloaded;
	}
	public void setIsDownloaded(String isDownloaded) {
		this.isDownloaded = isDownloaded;
	}
	@Override
	public String toString() {
		return "EdiOrderRequst [uploadStartTime=" + uploadStartTime + ", uploadEndTime=" + uploadEndTime
				+ ", downloadStartTime=" + downloadStartTime + ", downloadEndTime=" + downloadEndTime
				+ ", isDownloaded=" + isDownloaded + ", getSupplierCode()=" + getSupplierCode() + ", getOrderId()="
				+ getOrderId() + ", getSupplier()=" + getSupplier() + ", getFileName()=" + getFileName()
				+ ", getFileUrl()=" + getFileUrl() + ", getUploadTime()=" + getUploadTime() + ", getLastDownloadTime()="
				+ getLastDownloadTime() + ", getDownloadTimes()=" + getDownloadTimes() + ", getStartIndex()="
				+ getStartIndex() + ", getRows()=" + getRows() + ", getPage()=" + getPage() + ", getTotalRecord()="
				+ getTotalRecord() + ", getStatus()=" + getStatus() + ", getType()=" + getType()
				+ ", getLastModifyTime()=" + getLastModifyTime() + ", getLastModifyUser()=" + getLastModifyUser()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
	
	
}
