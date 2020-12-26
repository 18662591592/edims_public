package com.bgi.edims.model;

public class EdiAsnRequest extends EdiAsn{
	private String uploadStartTime;
	private String uploadEndTime;
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
	@Override
	public String toString() {
		return "EdiAsnRequest [uploadStartTime=" + uploadStartTime + ", uploadEndTime=" + uploadEndTime
				+ ", getUserId()=" + getUserId() + ", getSupplierCode()=" + getSupplierCode() + ", getAsnId()="
				+ getAsnId() + ", getSupplier()=" + getSupplier() + ", getFileName()=" + getFileName()
				+ ", getFileUrl()=" + getFileUrl() + ", getUploadUser()=" + getUploadUser() + ", getUploadTime()="
				+ getUploadTime() + ", getDownloadTimes()=" + getDownloadTimes() + ", getStartIndex()="
				+ getStartIndex() + ", getRows()=" + getRows() + ", getPage()=" + getPage() + ", getTotalRecord()="
				+ getTotalRecord() + ", getStatus()=" + getStatus() + ", getType()=" + getType()
				+ ", getLastModifyTime()=" + getLastModifyTime() + ", getLastModifyUser()=" + getLastModifyUser()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
}
