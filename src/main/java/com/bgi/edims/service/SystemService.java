package com.bgi.edims.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
@Service
public class SystemService {
	private Logger logger=LoggerFactory.getLogger(SystemService.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private String getCurrentTimestamp() {
        return sdf.format(new Date())+"-";
	}
	/**
	 * 创建文件
	 * @param file
	 * @throws IOException
	 */
	private void createFile(File file) throws IOException {
        if (file.exists()) {
        	file.delete();
        }
        file.createNewFile();
	}

	

	private Gson defaultGson=new GsonBuilder().create();

	public String getExtensionName(String fileName) {
		String extensionName = fileName.substring(fileName.lastIndexOf(".")+1);
		return extensionName;
	}
	

	public Integer excuteCommand(String cmd) throws Exception  {
		logger.info("[执行系统命令]"+cmd);
        if(SystemUtils.IS_OS_LINUX){
        	logger.info("[执行系统命令]当前操作系统linux");
            try {
                // 使用Runtime来执行command，生成Process对象
                Process process = Runtime.getRuntime().exec(
                        new String[] { "/bin/sh", "-c", cmd });
                new ProcessStreamThread(process.getErrorStream(),"error").start();
                new ProcessStreamThread(process.getInputStream(),"").start();
                int exitCode = process.waitFor();
                logger.info("[执行系统命令]exitCode=="+exitCode);
                return exitCode;
            } catch (java.lang.NullPointerException e) {
            	e.printStackTrace();
            } catch (java.io.IOException e) {
            	e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else if(SystemUtils.IS_OS_WINDOWS){
        	logger.info("[执行系统命令]当前操作系统windows");
            Process process;
            try {
                //process = new ProcessBuilder(cmd).start();
                String[] param_array = cmd.split("[\\s]+");
                ProcessBuilder pb = new ProcessBuilder(param_array);
                process = pb.start();
                new ProcessStreamThread(process.getErrorStream(),"error").start();
                new ProcessStreamThread(process.getInputStream(),"").start();
                /*process=Runtime.getRuntime().exec(cmd);*/
                int exitCode = process.waitFor();
                logger.info("[执行系统命令]exitCode=="+exitCode);
                return exitCode;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else {
        	throw new Exception("不支持本操作系统");
        }
        
        return null;
    }




	public static String getIpAddress() {
	    try {
	      Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
	      InetAddress ip = null;
	      while (allNetInterfaces.hasMoreElements()) {
		NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
		if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
		  continue;
		} else {
		  Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
		  while (addresses.hasMoreElements()) {
		    ip = addresses.nextElement();
		    if (ip != null && ip instanceof Inet4Address) {
			return ip.getHostAddress();
		    }
		  }
		}
	      }
	    } catch (Exception e) {
		System.err.println("IP地址获取失败" + e.toString());
	    }
	    return "";
	}

	public class ProcessStreamThread extends Thread {  
	    InputStream is;  
	    String type;
	    public ProcessStreamThread(InputStream is,String type) {  
	        this.is = is;  
	        this.type=type;
	    }  
	    public void run() {  
	        try {  
	            InputStreamReader isr = new InputStreamReader(is);  
	            BufferedReader br = new BufferedReader(isr);  
	            String line = null;  
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                isr.close();
                br.close();
                logger.info("[命令执行"+type+"]"+sb.toString());
	        } catch (IOException ioe) {  
	            ioe.printStackTrace();  
	        }  
	    }  
	}  
	

	
}
