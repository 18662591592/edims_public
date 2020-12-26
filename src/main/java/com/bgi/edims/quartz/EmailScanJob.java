package com.bgi.edims.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bgi.edims.mapper.OrderMapper;
import com.bgi.edims.mapper.UserMapper;
import com.bgi.edims.model.EdiOrder;
import com.bgi.edims.model.EdiOrderRequst;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.service.EmailService;
import com.bgi.edims.util.UtilTools;
@DisallowConcurrentExecution
public class EmailScanJob  implements Job{

	private Logger logger=LoggerFactory.getLogger(EmailScanJob.class);
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private EmailService emailService;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("---------邮件扫描定时器-----------");
		EdiOrderRequst ediOrderRequst=new EdiOrderRequst();
		ediOrderRequst.setIsDownloaded("0");
		ediOrderRequst.setDownloadTimes(0);
		List<EdiOrder> unDownloadOrders=orderMapper.getEdiOrders(ediOrderRequst);
		if(null!=unDownloadOrders&&unDownloadOrders.size()>0) {
			Map<String, List<EdiOrder>> ediOrderMap=new HashMap<>();
			for(EdiOrder ediOrder:unDownloadOrders) {
				String supplierCode=ediOrder.getSupplierCode();
				if(ediOrderMap.containsKey(supplierCode)) {
					ediOrderMap.get(supplierCode).add(ediOrder);
				}else {
					List<EdiOrder> ediOrders=new ArrayList<>();
					ediOrders.add(ediOrder);
					ediOrderMap.put(supplierCode, ediOrders);
				}
			}
			
			for(String supplierCode:ediOrderMap.keySet()) {
				if(StringUtils.isNotEmpty(supplierCode)) {
					EdiUser ediUser=new EdiUser();
					ediUser.setSupplierCode(supplierCode);
					List<EdiUser> ediUsers=userMapper.getBaseEdiUsers(ediUser);	
					for(EdiUser ediUser2:ediUsers) {
						String email=ediUser2.getEmail();
						if(UtilTools.isEmail(email)) {
							List<EdiOrder> ediOrders=ediOrderMap.get(supplierCode);
							int innerSize=0;
							int orderSize=0;
							for(EdiOrder ediOrder:ediOrders) {
								if(EdiOrder.TYPE.INNER.equals(ediOrder.getType())) {
									innerSize++;
								}
								if(EdiOrder.TYPE.NORMAL.equals(ediOrder.getType())) {
									orderSize++;
								}
							}
							String emailContext="您好：\n          截至邮件发送时间，您有"+(orderSize>0?orderSize+"个采购订单":"")+(innerSize>0?innerSize+"个内示订单":"")+"尚未下载，请及时处理";
//							您好：截至邮件发送时间，您有X个采购订单，Y个内示订单未下载！
							emailService.sendEmail(email, "TRCF订单未下载通知邮件", emailContext);
						}
					}
				}
			}
		}else {
			logger.info("待下载订单数量为空，定时器结束");
		}
	}

}
