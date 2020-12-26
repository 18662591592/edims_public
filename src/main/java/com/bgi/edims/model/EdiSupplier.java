package com.bgi.edims.model;
/**
 * 供应商信息
 * @author yeyuanchun
 *
 */
public class EdiSupplier  extends EdiBaseBean{

	private String supplierId;//供应商id
	private String supplierCode;//供应商编码
	private String supplierName;//供应商名称
	private String supplierAddr;//供应商地址
	private String linkmanName;//联系人名称
	private String linkmanMobile;//联系人电话
	public interface STATUS{
		public static String NORMAL="1";
		public static String FORZEN="-1";
	}
	public interface TYPE{
		public static String NORMAL="1";
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierAddr() {
		return supplierAddr;
	}
	public void setSupplierAddr(String supplierAddr) {
		this.supplierAddr = supplierAddr;
	}
	public String getLinkmanName() {
		return linkmanName;
	}
	public void setLinkmanName(String linkmanName) {
		this.linkmanName = linkmanName;
	}
	public String getLinkmanMobile() {
		return linkmanMobile;
	}
	public void setLinkmanMobile(String linkmanMobile) {
		this.linkmanMobile = linkmanMobile;
	}
	@Override
	public String toString() {
		return "EdiSupplier [supplierId=" + supplierId + ", supplierCode=" + supplierCode + ", supplierName="
				+ supplierName + ", supplierAddr=" + supplierAddr + ", linkmanName=" + linkmanName + ", linkmanMobile="
				+ linkmanMobile + "]";
	}
	
	
}
