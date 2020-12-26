package com.bgi.edims.model;

import java.util.Date;
/**
 * 订单对象
 * @author yeyuanchun
 *
 */
public class EdiOrder  extends EdiBaseBean{

	private String orderId;//订单ID
	private EdiSupplier supplier;//供应商
	private String fileName;//文件名称
	private String fileUrl;//文件路径
	private Date uploadTime;//上传时间
	private String supplierCode;//
	private Date lastDownloadTime;//最后下载时间
	private Integer downloadTimes;//下载次数
	public interface STATUS{
		public static String NORMAL="1";
		public static String FORZEN="-1";
	}
	public interface TYPE{
		public static String NORMAL="2";//采购订单
		public static String INNER="1";//内示订单
	}
	
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Date getLastDownloadTime() {
		return lastDownloadTime;
	}
	public void setLastDownloadTime(Date lastDownloadTime) {
		this.lastDownloadTime = lastDownloadTime;
	}
	public Integer getDownloadTimes() {
		return downloadTimes;
	}
	public void setDownloadTimes(Integer downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
	@Override
	public String toString() {
		return "EdiOrder [orderId=" + orderId + ", supplier=" + supplier + ", fileName=" + fileName + ", fileUrl="
				+ fileUrl + ", uploadTime=" + uploadTime + ", supplierCode=" + supplierCode + ", lastDownloadTime="
				+ lastDownloadTime + ", downloadTimes=" + downloadTimes + "]";
	}
	
	
}
