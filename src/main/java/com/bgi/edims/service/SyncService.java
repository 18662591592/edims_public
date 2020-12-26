package com.bgi.edims.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bgi.edims.mapper.AsnMapper;
import com.bgi.edims.mapper.OrderMapper;
import com.bgi.edims.mapper.SupplierMapper;
import com.bgi.edims.mapper.SysConfigMapper;
import com.bgi.edims.model.EdiAsn;
import com.bgi.edims.model.EdiAsnRequest;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiOrder;
import com.bgi.edims.model.EdiSupplier;
import com.bgi.edims.model.EdiSysConfig;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.sftp.FTPChannel;
import com.bgi.edims.sftp.PresapSftpChannel;
@Service
public class SyncService {
//    @Autowired
//    private SftpChannelPool sftpChannelPool;
//    @Autowired
//    private FTPClientPool ftpClientPool;
    private Logger logger=LoggerFactory.getLogger(SyncService.class);
	@Value("${edi.path.asn}")
	private String asnPath;
	@Value("${edi.path.inner}")
	private String innerPath;
	@Value("${edi.path.order}")
	private String orderPath;
	@Autowired
	private SupplierMapper supplierMapper;
	@Autowired
	private OrderMapper orderMapper;
    @Autowired
    private SysConfigMapper sysConfigMapper;
    @Autowired
    private AsnMapper asnMapper;
    @Autowired
    private EmailService emailService;
    public Set<String> getAllSuppliers() {
    	List<EdiSupplier> suppliers=supplierMapper.getEdiSuppliers(new EdiSupplier());
    	Set<String> supplierCodes=new HashSet<>();
    	for(EdiSupplier ediSupplier:suppliers) {
    		supplierCodes.add(ediSupplier.getSupplierCode());
    	}
		return supplierCodes;
    }
    public Map<String, String> getSystemConfigMap(){
    	Map<String, String> systemConfigs=new HashMap<>();
		EdiSysConfig paramConfig=new EdiSysConfig();
		List<EdiSysConfig> ediSysConfigs=sysConfigMapper.getEdiSysConfigs(paramConfig);
		for(EdiSysConfig ediSysConfig:ediSysConfigs) {
			systemConfigs.put(ediSysConfig.getConfigKey(), ediSysConfig.getConfigValue());
		}
		return systemConfigs;
    }
    
    public boolean renameFile(String path,String file,FTPChannel ftpChannel) throws IOException {
		String newFileName="";
		if(file.contains(".")) {
			int index=file.lastIndexOf(".");
			newFileName=file.substring(0, index)+"_err"+file.substring(index);
		}else {
			newFileName=file+"_err";
		}
		return ftpChannel.renameFile(path,file, newFileName);
    }
    
    public void renameFile(String file,PresapSftpChannel presapSftpChannel) {
		String newFileName="";
		if(file.contains(".")) {
			int index=file.lastIndexOf(".");
			newFileName=file.substring(0, index)+"_err"+file.substring(index);
		}else {
			newFileName=file+"_err";
		}
		presapSftpChannel.renameFile(file, newFileName);
    }
	@EdiMethod(value="syncOrders")
	public void syncOrders(HttpServletRequest request,HttpServletResponse response,EdiUser currentUser) {
		logger.info("[EdiMethod:]");
		String type=request.getParameter("type");
		Map<String, String> systemConfigs=getSystemConfigMap();
		if("1".equals(type)) {
			String path=systemConfigs.get("inner_order_scan_path");
			syncInnerOrders(path);
		}else if("2".equals(type)) {
			String path=systemConfigs.get("order_scan_path");
			syncOrders(path, false);
		}
	}
	public void uploadAsn(EdiAsn ediAsn) {

		try {
			Map<String, String> systemConfigs=getSystemConfigMap();
			String path=systemConfigs.get("asn_scan_path");
			FTPChannel ftpChannel=null;
			try {
				ftpChannel=new FTPChannel();
			} catch (Exception e) {
			    // TODO: handle exception
				e.printStackTrace();
			}
			 boolean result=ftpChannel.changeWorkingDirectory(path);
		    if(!result) {
		    	throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "文件夹路径"+path+"不可用，请检查文件夹配置");
		    }
		    File asnFile=new File(ediAsn.getFileUrl());
		    ftpChannel.uploadFile(path,ediAsn.getFileName(), new FileInputStream(asnFile));
			if (ftpChannel.ftpClient.isConnected()) {
				try {
					ftpChannel.ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		    
		    
		    ediAsn.setStatus("2");
		    asnMapper.updateEdiAsn(ediAsn);
		} catch (Exception e) {
		    // TODO: handle exception
			e.printStackTrace();
		}
//		PooledObject<Session> pooledSession=sftpChannelPool.borrowObject();
//		try {
//			Map<String, String> systemConfigs=getSystemConfigMap();
//			String path=systemConfigs.get("asn_scan_path");
//		    PresapSftpChannel presapSftpChannel=null;
//			try {
//			    presapSftpChannel=new PresapSftpChannel(pooledSession);
//			} catch (Exception e) {
//			    // TODO: handle exception
//			    pooledSession=sftpChannelPool.getReplacePooledObject(pooledSession);
//			}
//		    boolean result=presapSftpChannel.changeDir(path);
//		    if(!result) {
//		    	throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "文件夹路径"+path+"不可用，请检查文件夹配置");
//		    }
//		    File asnFile=new File(ediAsn.getFileUrl());
//		    presapSftpChannel.uploadFile(path,ediAsn.getFileName(), new FileInputStream(asnFile));
//		    ediAsn.setStatus("2");
//		    asnMapper.updateEdiAsn(ediAsn);
//		    presapSftpChannel.channel.disconnect();
//		} catch (Exception e) {
//		    // TODO: handle exception
//			e.printStackTrace();
//		}finally{
//			sftpChannelPool.returnObject(pooledSession);
//		}
	}
	
	public void uploadAsns(String path) {

		

		try {
			FTPChannel ftpChannel=null;
			try {
				ftpChannel=new FTPChannel();
			} catch (Exception e) {
			    // TODO: handle exception
				e.printStackTrace();
			}
		    boolean result=ftpChannel.changeWorkingDirectory(path);
		    if(!result) {
		    	throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "文件夹路径"+path+"不可用，请检查文件夹配置");
		    }
		    EdiAsnRequest paramAsn=new EdiAsnRequest();
		    paramAsn.setStatus("1");
		    List<EdiAsn> asns=asnMapper.getEdiAsns(paramAsn);
		    if(null!=asns&&asns.size()>0) {
		    	for(EdiAsn ediAsn:asns) {
		    		File asnFile=new File(ediAsn.getFileUrl());
		    		ftpChannel.uploadFile(path,ediAsn.getFileName(), new FileInputStream(asnFile));
		    		ediAsn.setStatus("2");
		    		asnMapper.updateEdiAsn(ediAsn);
		    	}
		    	
		    }
			if (ftpChannel.ftpClient.isConnected()) {
				try {
					ftpChannel.ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		    // TODO: handle exception
			e.printStackTrace();

		}
		
	}
	public void uploadAsnFiles(MultipartFile[]  files,EdiUser ediUser) throws EdiException {
        //批量上传
        for(int i = 0;i<files.length;i++){  
            if(!files[i].getOriginalFilename().isEmpty()) {            	
                String path = asnPath+"/"+files[i].getOriginalFilename();
//                String path = "D://temp"+"/"+files[i].getOriginalFilename();
                File newFile=new File(path);
                try {
                    if(!newFile.getParentFile().exists()){
                    	newFile.getParentFile().mkdir();
                    }
                    files[i].transferTo(newFile);
                    EdiAsn ediAsn=new EdiAsn();
                    ediAsn.setFileName(files[i].getOriginalFilename());
                    ediAsn.setFileUrl(path);
                    ediAsn.setSupplierCode(ediUser.getSupplierCode());
                    ediAsn.setUserId(ediUser.getUserId());
                    asnMapper.createEdiAsn(ediAsn);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                }
            }else if(files.length == 1){
                throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "上传的文件不能为空");
            }
         }
	}
	public void syncInnerOrders(String path) {	
		logger.info("[销售订单轮询]启动内示订单轮询 "+path);
		try {
			FTPChannel ftpChannel=null;
			try {
				ftpChannel=new FTPChannel();
			} catch (Exception e) {
			    // TODO: handle exception
				e.printStackTrace();
			}
		    boolean result=ftpChannel.changeWorkingDirectory(path);
		    if(!result) {
		    	throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "文件夹路径"+path+"不可用，请检查文件夹配置");
		    }
            String[] files = ftpChannel.listFileNames(path);

		    if(null==files||files.length==0) {
		    	return;
		    }
		    List<String> dealingFiles=new ArrayList<>();
		    for(String file:files) {
		    	if(file.contains("_err")||file.endsWith("lnk")) {
		    		continue;
		    	}
		    	dealingFiles.add(file);
		    }
		    if(null!=dealingFiles&&dealingFiles.size()>0) {
			    Set<String> suppliers=getAllSuppliers();
			    for(String file:dealingFiles) {
			    	boolean isMatch=false;
			    	for(String supplierCode:suppliers) {
			    		if(file.startsWith(supplierCode)) {
			    			ftpChannel.downloadFile(path, file, innerPath);
			    			ftpChannel.deleteFile(path, file);
						    EdiOrder ediOrder=new EdiOrder();
						    ediOrder.setFileName(file);
						    ediOrder.setFileUrl(innerPath+"/"+file);
						    ediOrder.setSupplierCode(supplierCode);
						    ediOrder.setType(EdiOrder.TYPE.INNER);
						    orderMapper.createEdiOrder(ediOrder);
			    			isMatch=true;
			    			break;
			    		}
			    	}
			    	if(!isMatch) {
			    		if(renameFile(path,file, ftpChannel)) {
			    			emailService.sentEmailToAllAdmins(true?"内示订单":"销售订单"+file+"上传失败", true?"内示订单":"销售订单"+file+"上传失败，请检查文件名称是否正确");
			    		}else {
							System.out.println("文件"+path+"-"+file+"重命名失败");
						};
			    		
			    		continue;
			    	}
			    }
		    }
			if (ftpChannel.ftpClient.isConnected()) {
				try {
					ftpChannel.ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		    // TODO: handle exception
			e.printStackTrace();
		}
	}
	public void syncOrders(String path,boolean inner) {		
		logger.info("[销售订单轮询]启动销售订单轮询 "+path);
		try {
			FTPChannel ftpChannel=null;
			try {
				ftpChannel=new FTPChannel();
			} catch (Exception e) {
			    // TODO: handle exception
				e.printStackTrace();
			}
		    boolean result=ftpChannel.changeWorkingDirectory(path);
		    if(!result) {
		    	throw new EdiException(EdiException.ERROR_CODE.ILLEGAL, "文件夹路径"+path+"不可用，请检查文件夹配置");
		    }
            String[] files = ftpChannel.listFileNames(path);

		    if(null==files||files.length==0) {
		    	return;
		    }
		    List<String> dealingFiles=new ArrayList<>();
		    for(String file:files) {
		    	if(file.contains("_err")||file.endsWith("lnk")) {
		    		continue;
		    	}
		    	/**
		    	 * 限制文件必须为excel文件
		    	 */
//		    	if(!file.endsWith(".xls")&&!file.endsWith(".csv")&&!file.endsWith(".xlsx")) {
//		    		renameFile(path,file, ftpChannel);
//		    		continue;
//		    	}
		    	dealingFiles.add(file);
		    }
		    if(null!=dealingFiles&&dealingFiles.size()>0) {
			    Set<String> suppliers=getAllSuppliers();
			    for(String file:dealingFiles) {
			    	boolean isMatch=false;
			    	for(String supplierCode:suppliers) {
			    		if(file.startsWith(supplierCode)) {
			    			if(inner) {
			    				ftpChannel.downloadFile(path, file, innerPath);
			    				ftpChannel.deleteFile(path, file);
						    	EdiOrder ediOrder=new EdiOrder();
						    	ediOrder.setFileName(file);
						    	ediOrder.setFileUrl(innerPath+"/"+file);
						    	ediOrder.setSupplierCode(supplierCode);
						    	ediOrder.setType(EdiOrder.TYPE.INNER);
						    	orderMapper.createEdiOrder(ediOrder);
			    			}else {
			    				ftpChannel.downloadFile(path, file, orderPath);
			    				ftpChannel.deleteFile(path, file);
						    	EdiOrder ediOrder=new EdiOrder();
						    	ediOrder.setFileName(file);
						    	ediOrder.setFileUrl(orderPath+"/"+file);
						    	ediOrder.setSupplierCode(supplierCode);
						    	ediOrder.setType(EdiOrder.TYPE.NORMAL);
						    	orderMapper.createEdiOrder(ediOrder);
			    			}
			    			isMatch=true;
			    			break;
			    		}
			    	}
			    	if(!isMatch) {
			    		if(renameFile(path,file, ftpChannel)) {
			    			emailService.sentEmailToAllAdmins(inner?"内示订单":"销售订单"+file+"上传失败", inner?"内示订单":"销售订单"+file+"上传失败，请检查文件名称是否正确");
			    		}else {
							System.out.println("文件"+path+"-"+file+"重命名失败");
						};
			    		
			    		continue;
			    	}
			    }
		    }
			if (ftpChannel.ftpClient.isConnected()) {
				try {
					ftpChannel.ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		    // TODO: handle exception
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String file="12345.xls";
		String newFileName="";
		if(file.contains(".")) {
			int index=file.lastIndexOf(".");
			newFileName=file.substring(0, index)+"_err"+file.substring(index);
		}else {
			newFileName=file+"_err";
		}
		System.out.println(newFileName);
	}
}
