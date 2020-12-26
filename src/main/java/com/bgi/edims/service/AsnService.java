package com.bgi.edims.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import com.bgi.edims.mapper.AsnMapper;
import com.bgi.edims.model.EdiAsn;
import com.bgi.edims.model.EdiAsnRequest;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.util.IOtools;
@Service
public class AsnService {
	@Autowired
	private AsnMapper asnMapper;
	@Value("${edi.asnmodel}")
	public String asnmodel ;
	@Value("${edi.roleid}")
	private String roleId;
	private Logger logger=LoggerFactory.getLogger(AsnService.class);
	@EdiMethod(value="getEdiAsns")
	public EdiResponse<List<EdiAsn>> getEdiAsns(EdiAsnRequest ediAsnRequest ,EdiUser currentUser) throws EdiException {
		logger.info("[EdiMethod:getEdiAsns]获取ASN列表");
		EdiResponse<List<EdiAsn>> ediResponse=new EdiResponse<>();
		try {
			
			if(StringUtils.isNotEmpty(currentUser.getRoleId())) {
				if(!roleId.equals(currentUser.getRoleId())) {
					String supplierCode=currentUser.getSupplierCode();
					if(StringUtils.isEmpty(supplierCode)) {
						ediAsnRequest.setSupplierCode("supplierCode");
					}else {
						ediAsnRequest.setSupplierCode(supplierCode);
					}
				}
			}else {
				ediAsnRequest.setSupplierCode("supplierCode");
			}
			logger.info("[EdiMethod:getEdiAsns]参数="+ediAsnRequest.toString());
			Integer total=asnMapper.getEdiAsnsCount(ediAsnRequest);
			ediResponse.setTotal(total);
			List<EdiAsn> ediAsns=asnMapper.getEdiAsns(ediAsnRequest);
			ediResponse.setRows(ediAsns);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
			ediResponse.setMsg("获取ASN列表出现异常");
		}
		logger.info("[EdiMethod:getEdiAsns]返回="+ediResponse.toString());
		return ediResponse;
	}

	
	@EdiMethod(value="deleteEdiAsns",permission="3delete")
	public void deleteEdiAsns(HttpServletRequest request,HttpServletResponse response,EdiUser currentUser) {
		logger.info("[EdiMethod:deleteEdiAsns]删除ASN信息");
		String asnIdIds=request.getParameter("asnIds");
		logger.info("[EdiMethod:deleteEdiAsns]入参="+asnIdIds);
		String[] asnIdIdArray=asnIdIds.split(",");
		asnMapper.deleteEdiAsns(asnIdIdArray);
	}
	
	public void downloadAsnModel(HttpServletRequest request,HttpServletResponse response) {
		String fileName=asnmodel.substring(asnmodel.lastIndexOf("/")+1);
		response.setHeader("content-type", "application/octet-stream");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        BufferedInputStream bis = null;
        OutputStream os = null;
        byte[] buff = new byte[1024];
      try {
    	os = response.getOutputStream();
      	for(int i=0;i<3;i++) {
            bis = new BufferedInputStream(new FileInputStream(new File(asnmodel)));
            int j = bis.read(buff);
            while (j != -1) {
              os.write(buff, 0, buff.length);
              os.flush();
              j = bis.read(buff);
            }
      	}
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (bis != null) {
          try {
            bis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public void downloadAsnZip(HttpServletRequest request,HttpServletResponse response) {
    	try {
    		String asnIdIds=request.getParameter("asnIds");
    		if(StringUtils.isEmpty(asnIdIds)){
    			return ;
    		}
    		String[] asnIdIdArray=asnIdIds.split(",");
    		List<EdiAsn> ediAsns=asnMapper.getEdiAsnsByAsnIds(asnIdIdArray);
    		List<File> files=new ArrayList<>();
    		Set<String> fileNames=new HashSet<>();
    		for(EdiAsn ediAsn:ediAsns) {
    			File asnFile=new File(ediAsn.getFileUrl());
    			if(asnFile.exists()) {
    				if(fileNames.contains(ediAsn.getFileName())) {
    					continue;
    				}
    				fileNames.add(ediAsn.getFileName());
    				files.add(asnFile);
    			}
    		}
    		File fileZip = new File("./"+sdf.format(new Date())+".zip");
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
