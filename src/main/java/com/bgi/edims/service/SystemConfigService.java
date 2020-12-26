package com.bgi.edims.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.SysConfigMapper;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiSysConfig;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.quartz.QuartzManagerService;
import com.bgi.edims.sftp.FTPChannel;

@Service
public class SystemConfigService {
	private Logger logger=LoggerFactory.getLogger(SystemConfigService.class);
	@Autowired
	private SysConfigMapper sysConfigMapper;
//	@Autowired
//	private EmailService emailService;
	@Autowired
	private QuartzManagerService quartzManagerService;
	@EdiMethod(value="getSystemConfigs")
	public List<EdiSysConfig> getSystemConfigs(HttpServletRequest request){
		String type=request.getParameter("tpye");
		return getSystemConfigs(type);
	}
	
	public List<EdiSysConfig> getSystemConfigs(String type){
		EdiSysConfig paramConfig=new EdiSysConfig();
		paramConfig.setType(type);
		List<EdiSysConfig> ediSysConfigs=sysConfigMapper.getEdiSysConfigs(paramConfig);

		return ediSysConfigs;
	}
	@EdiMethod(value="updateEmailConfig",permission="8update")
	public void updateEmailConfig(HttpServletRequest request,EdiUser currentUser) {
		logger.info("[EdiMethod:]");
		String emailhost=request.getParameter("emailhost");
		String emailport=request.getParameter("emailport");
		String emailaddr=request.getParameter("emailaddr");
		String emailpwd=request.getParameter("emailpwd");
		String email_scan_interval=request.getParameter("email_scan_interval");
		EdiSysConfig emailhostConfig=new EdiSysConfig("emailhost", emailhost, currentUser);
		EdiSysConfig emailportConfig=new EdiSysConfig("emailport", emailport, currentUser);
		EdiSysConfig emailaddrConfig=new EdiSysConfig("emailaddr", emailaddr, currentUser);
		EdiSysConfig emailpwdConfig=new EdiSysConfig("emailpwd", emailpwd, currentUser);
		EdiSysConfig emailScanIntervalConfig=new EdiSysConfig("email_scan_interval", email_scan_interval, currentUser);
		sysConfigMapper.updateEdiSysConfig(emailhostConfig);
		sysConfigMapper.updateEdiSysConfig(emailportConfig);
		sysConfigMapper.updateEdiSysConfig(emailaddrConfig);
		sysConfigMapper.updateEdiSysConfig(emailpwdConfig);
		sysConfigMapper.updateEdiSysConfig(emailScanIntervalConfig);
		quartzManagerService.startEmailScanJob();
	}
	
	@EdiMethod(value="updateOrderConfig",permission="7update")
	public void updateOrderConfig(HttpServletRequest request,EdiUser currentUser) {
		logger.info("[EdiMethod:]");
		String order_scan_path=request.getParameter("order_scan_path");
		String order_scan_interval=request.getParameter("order_scan_interval");
		EdiSysConfig order_scan_interval_config=new EdiSysConfig("order_scan_interval", order_scan_interval, currentUser);
		EdiSysConfig order_scan_path_config=new EdiSysConfig("order_scan_path", order_scan_path, currentUser);
//		System.out.println("update"+order_scan_interval_config.toString()+order_scan_path_config.toString());
		sysConfigMapper.updateEdiSysConfig(order_scan_interval_config);
		sysConfigMapper.updateEdiSysConfig(order_scan_path_config);
		quartzManagerService.startOrderScanJob();
	}
	@EdiMethod(value="updateInnerOrderConfig",permission="7update")
	public void updateInnerOrderConfig(HttpServletRequest request,EdiUser currentUser) {
		logger.info("[EdiMethod:]");
		String inner_order_scan_path=request.getParameter("inner_order_scan_path");
		String inner_order_scan_interval=request.getParameter("inner_order_scan_interval");
		EdiSysConfig inner_order_scan_interval_config=new EdiSysConfig("inner_order_scan_interval", inner_order_scan_interval, currentUser);
		EdiSysConfig inner_order_scan_path_config=new EdiSysConfig("inner_order_scan_path", inner_order_scan_path, currentUser);
//		System.out.println("update"+inner_order_scan_interval_config.toString()+inner_order_scan_path_config.toString());
		sysConfigMapper.updateEdiSysConfig(inner_order_scan_interval_config);
		sysConfigMapper.updateEdiSysConfig(inner_order_scan_path_config);
		quartzManagerService.startInnerOrderScanJob();
	}
	@EdiMethod(value="updateAsnConfig",permission="7update")
	public void updateAsnConfig(HttpServletRequest request,EdiUser currentUser) {
		logger.info("[EdiMethod:]");
		String asn_scan_path=request.getParameter("asn_scan_path");
		String asn_scan_interval=request.getParameter("asn_scan_interval");
		EdiSysConfig asn_scan_path_config=new EdiSysConfig("asn_scan_path", asn_scan_path, currentUser);
		EdiSysConfig asn_scan_interval_config=new EdiSysConfig("asn_scan_interval", asn_scan_interval, currentUser);
//		System.out.println("update"+asn_scan_interval_config.toString()+asn_scan_interval_config.toString());
		sysConfigMapper.updateEdiSysConfig(asn_scan_path_config);
		sysConfigMapper.updateEdiSysConfig(asn_scan_interval_config);
		
		quartzManagerService.startAsnScanJob();
	}
	
	@EdiMethod(value="testEmailConfig")
	public void testEmailConfig(HttpServletRequest request,EdiUser currentUser) {
		logger.info("[EdiMethod:]");
//		String emailhost=request.getParameter("emailhost");
//		String emailport=request.getParameter("emailport");
//		String emailaddr=request.getParameter("emailaddr");
//		String emailpwd=request.getParameter("emailpwd");
//		emailService.sendEmail("", "");
	}
	@EdiMethod(value="testScanPath")
	public void testScanPath(HttpServletRequest request) throws EdiException {
		logger.info("[EdiMethod:]");
		String path=request.getParameter("path");
		FTPChannel ftpChannel=new FTPChannel();
		try {

		    boolean result=ftpChannel.changeWorkingDirectory(path);
		    if(!result) {
		    	throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "文件路径不可用");
		    }
		} catch (EdiException e) {
			throw e;
		} catch (Exception e) {
		    // TODO: handle exception
			throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "网络异常，请检查FTP服务器连接");
		}finally {
			if (ftpChannel.ftpClient.isConnected()) {
				try {
					ftpChannel.ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
}
