package com.bgi.edims.sftp;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.edims.model.EdiException;
public class FTPClientFactory implements PooledObjectFactory<FTPClient> {
private static Logger logger = LoggerFactory.getLogger("file");
 private  FTPClientConfigure config;
 //给工厂传入一个参数对象，方便配置FTPClient的相关参数
 public FTPClientFactory(FTPClientConfigure config){
      this.config=config;
 }


 public PooledObject<FTPClient> makeObject() throws Exception {
      FTPClient ftpClient = new FTPClient();
      ftpClient.setConnectTimeout(config.getClientTimeout());
      try {
           ftpClient.connect(config.getHost(), config.getPort());
//           ftpClient.connect(config.getHost(), config.getPort());
           int reply = ftpClient.getReplyCode();
           if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                logger.warn("FTPServer refused connection");
                return null;
           }
           boolean result = ftpClient.login(config.getUsername(), config.getPassword());
           if (!result) {
                throw new EdiException("ftpClient登陆失败! userName:" + config.getUsername() + " ; password:" + config.getPassword());
           }
           ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
           ftpClient.setBufferSize(1024);
           ftpClient.setControlEncoding(config.getEncoding());
           ftpClient.enterLocalPassiveMode();
           if (config.getPassiveMode().equals("true")) {
                ftpClient.enterLocalPassiveMode();
           }
      } catch (IOException e) {
           e.printStackTrace();
      } catch (Exception e) {
           e.printStackTrace();
      }
      return (PooledObject<FTPClient>) new DefaultPooledObject<FTPClient>(ftpClient);
 }



 /* (non-Javadoc)
 * @see org.apache.commons.pool.PoolableObjectFactory#destroyObject(java.lang.Object)
 */
 public void destroyObject(FTPClient ftpClient) throws Exception {
      try {
           if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
           }
      } catch (IOException io) {
           io.printStackTrace();
      } finally {
           // 注意,一定要在finally代码中断开连接，否则会导致占用ftp连接情况
           try {
                ftpClient.disconnect();
           } catch (IOException io) {
                io.printStackTrace();
           }
      }
 }

 /* (non-Javadoc)
 * @see org.apache.commons.pool.PoolableObjectFactory#validateObject(java.lang.Object)
 */
 public boolean validateObject(FTPClient ftpClient) {
      try {
           return ftpClient.sendNoOp();
      } catch (IOException e) {
           throw new RuntimeException("Failed to validate client: " + e, e);
      }
 }

 public void activateObject(FTPClient ftpClient) throws Exception {
 }

 public void passivateObject(FTPClient ftpClient) throws Exception {

 }





@Override
public void destroyObject(PooledObject<FTPClient> p) throws Exception {
	// TODO Auto-generated method stub
	
}


@Override
public boolean validateObject(PooledObject<FTPClient> p) {
	// TODO Auto-generated method stub
	if(p.getObject().isAvailable()) {
		return true;
	}
	return false;
}


@Override
public void activateObject(PooledObject<FTPClient> p) throws Exception {
	// TODO Auto-generated method stub
	
}


@Override
public void passivateObject(PooledObject<FTPClient> p) throws Exception {
	// TODO Auto-generated method stub
	
}

}