package com.bgi.edims.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.OrderMapper;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiOrder;
import com.bgi.edims.model.EdiOrderRequst;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.util.IOtools;

@Service
public class OrderService {
	@Autowired
	private OrderMapper orderMapper;
	private Logger logger=LoggerFactory.getLogger(OrderService.class);
	@Value("${edi.roleid}")
	private String roleId;
	
	
	@EdiMethod(value="getEdiOrders")
	public EdiResponse<List<EdiOrder>> getEdiOrders(EdiOrderRequst ediOrderRequst,EdiUser currentUser) {
		logger.info("[EdiMethod:getEdiOrders]获取订单列表");
		logger.info("[EdiMethod:getEdiOrders]入参="+ediOrderRequst.toString());
		EdiResponse<List<EdiOrder>> ediResponse=new EdiResponse<>();
		try {
			if(StringUtils.isNotEmpty(currentUser.getRoleId())) {
				if(!roleId.equals(currentUser.getRoleId())) {
					String supplierCode=currentUser.getSupplierCode();
					if(StringUtils.isEmpty(supplierCode)) {
						ediOrderRequst.setSupplierCode("supplierCode");
					}else {
						ediOrderRequst.setSupplierCode(supplierCode);
					}
				}
			}else {
				ediOrderRequst.setSupplierCode("supplierCode");
			}
			Integer total=orderMapper.getEdiOrdersCount(ediOrderRequst);
			ediResponse.setTotal(total);
			List<EdiOrder> ediOrders=orderMapper.getEdiOrders(ediOrderRequst);
			ediResponse.setRows(ediOrders);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
			ediResponse.setMsg("获取订单列表出现异常");
		}
		logger.info("[EdiMethod:getEdiOrders]返回="+ediResponse.toString());
		return ediResponse;
	}

	
	@EdiMethod(value="deleteEdiOrders")
	public void deleteEdiOrders(HttpServletRequest request,HttpServletResponse response,EdiUser currentUser) {
		logger.info("[EdiMethod:deleteEdiOrders]删除订单");
		String reportIds=request.getParameter("reportIds");
		logger.info("[EdiMethod:deleteEdiOrders]入参="+reportIds);
		String[] reportIdArray=reportIds.split(",");
		orderMapper.deleteEdiOrders(reportIdArray);
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
    public void downloadOrderZip(HttpServletRequest request,HttpServletResponse response,EdiUser currentUser) {
    	try {
    		String reportIds=request.getParameter("reportIds");
    		if(StringUtils.isEmpty(reportIds)){
    			return ;
    		}
    		String[] reportIdArray=reportIds.split(",");
    		List<EdiOrder> ediOrders=orderMapper.getEdiOrdersByOrderIds(reportIdArray);
    		List<File> files=new ArrayList<>();
    		Date downloadTime=new Date();
    		Set<String> fileNames=new HashSet<>();
    		for(EdiOrder ediOrder:ediOrders) {
    			File orderFile=new File(ediOrder.getFileUrl());
    			if(orderFile.exists()) {
    				if(fileNames.contains(ediOrder.getFileName())) {
    					continue;
    				}
    				files.add(orderFile);
    				EdiOrder existOrder=new EdiOrder();
    				existOrder.setDownloadTimes(ediOrder.getDownloadTimes()+1);
    				existOrder.setLastDownloadTime(downloadTime);
    				existOrder.setOrderId(ediOrder.getOrderId());
    				existOrder.setLastModifyUser(currentUser.getUserName());
    				existOrder.setLastModifyTime(downloadTime);
    				existOrder.setUploadTime(ediOrder.getUploadTime());
    				orderMapper.updateEdiOrder(existOrder);
    			}
    		}
    		File fileZip = new File("./"+sdf.format(downloadTime)+".zip");
        	FileOutputStream outStream = new FileOutputStream(fileZip);
            ZipOutputStream toClient = new ZipOutputStream(outStream);
            IOtools.zipFile(files,toClient);
            toClient.close();
            outStream.close();
            IOtools.downloadFile(fileZip,response,true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return;
    }
}
