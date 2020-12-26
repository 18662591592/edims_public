package com.bgi.edims.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.service.AsnService;
import com.bgi.edims.service.EdiMethodFactory;
import com.bgi.edims.service.EmailService;
import com.bgi.edims.service.OrderService;
import com.bgi.edims.service.SyncService;
import com.bgi.edims.service.SystemService;
import com.bgi.edims.service.UserService;
import com.bgi.edims.util.UtilTools;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
@RequestMapping("/intf")
public class EdiInterfaceController {
    @Autowired
    private EdiMethodFactory ediMethodFactory;
    private Logger logger=LoggerFactory.getLogger(EdiInterfaceController.class);
    private Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().contains("Pwd");
        }
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();
    @Autowired
    private EmailService emailService;
    @Autowired
    private SystemService systemService;
//    @Autowired
//    private SftpChannelPool sftpChannelPool;
//    @Autowired
//    private QuartzManagerService quartzManagerService;
	@RequestMapping("test")
	public String test(HttpServletRequest request) throws SchedulerException {
		emailService.sendEmail("644866551@qq.com","测试", "ceshiceshi ");
		return "success";
	}
	
	
	@RequestMapping("/restartService")
	public String restartService() {
		try {
			systemService.excuteCommand("/mnt/edims.sh restart");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}

	@Autowired
	public UserService userService;
    
    @RequestMapping("/commonintf")
    public void bispHpvCommonInterface(HttpServletRequest request, HttpServletResponse response) {
        @SuppressWarnings("rawtypes")
		EdiResponse ediResponse = new EdiResponse();
        try {
        	ediResponse = ediMethodFactory.executeMethod(request, response);
        } catch (EdiException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            ediResponse.setCode(e.getCode());
            ediResponse.setMsg(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
        	ediResponse.setMsg("系统异常");
        }
        String responseJson = gson.toJson(ediResponse);
        UtilTools.writeAjaxResult(request, response, responseJson);
    }
    
    @RequestMapping(value = "/downloadOrderFiles", method = RequestMethod.GET)
    public void downloadOrderFiles(HttpServletResponse res) {
        String fileName = "tibet.log";
        String fileUrl="D:/tibet.log";
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        BufferedInputStream bis = null;
        OutputStream os = null;
        byte[] buff = new byte[1024];
      try {
    	os = res.getOutputStream();
      	for(int i=0;i<3;i++) {
            bis = new BufferedInputStream(new FileInputStream(new File(fileUrl)));
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
	private EdiUser getCurrentUser() {
		Subject subject=SecurityUtils.getSubject();
		if(null==subject) {
			return null;
		}
		EdiUser ediUser= (EdiUser) subject.getPrincipal();
		return ediUser;
	}
    @Autowired
    private OrderService orderService;
    @Autowired
    private AsnService asnService;
    @RequestMapping(value = "/downloadOrderZip", method = RequestMethod.GET)
    public void downloadOrderZip(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		logger.info("--------------下载销售订单文件--------------");
        	EdiUser ediUser=getCurrentUser();
            orderService.downloadOrderZip(request, response,ediUser);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

    }
    @RequestMapping(value = "/downloadAsnZip", method = RequestMethod.GET)
    public void downloadAsnZip(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		logger.info("------------下载ASN文件--------------");
    		asnService.downloadAsnZip(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    @RequestMapping(value = "/downloadAsnModel", method = RequestMethod.GET)
    public void downloadAsnModel(HttpServletRequest request, HttpServletResponse response) {
    	try {
    		logger.info("--------------下载ASN模板--------------");
    		asnService.downloadAsnModel(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    @Autowired
    private SyncService syncService;

    /**
     * 上传文件
     * @param file
     * @return
     * @throws EdiException 
     */
	@RequestMapping(value = "/uploadAsnFiles", method = RequestMethod.POST)
    public EdiResponse uploadAsnFiles(@RequestParam("asnFiles") MultipartFile[]  files,HttpServletRequest request, HttpServletResponse response){
		EdiResponse ediResponse=new EdiResponse<>();
		try {
			logger.info("--------------上传ASN文件--------------");
			if(null==files||files.length==0) {
	    		throw new EdiException(EdiException.ERROR_CODE.ILLEGAL,"上传的文件不能为空");
	    	}
	    	syncService.uploadAsnFiles(files,getCurrentUser());	
		} catch (EdiException e) {
			ediResponse.setCode(e.getCode());
			ediResponse.setMsg(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
			ediResponse.setMsg("上传文件出现异常");
		}
		return ediResponse;

        }
}
