package com.bgi.edims.sftp;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.edims.model.EdiException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
public class SftpChannelFactory implements PooledObjectFactory<Session> {
	
	private Logger logger=LoggerFactory.getLogger(PresapSftpChannel.class);
	private SftpConfig sftpConfig;
	public SftpChannelFactory(SftpConfig sftpConfig){
		this.sftpConfig=sftpConfig;
	}
	public SftpConfig getSftpConfig(){
		return sftpConfig;
	}
	/**
	 * 创建ChannelSftp
	 * @return
	 * @throws OpenException
	 */
	public  Session getConnectedSession() throws EdiException {
		Session sshSession=null;

		/**
		 * session为空或者session未连接，重新新建session
		 */
		if(sshSession==null||!sshSession.isConnected()){
			try {
				/**
				 * 检查配置
				 */
				if(null==sftpConfig){
					logger.error("创建SFTP Channel异常,未配置sftpConfig");
					return sshSession;
				}
				if(StringUtils.isEmpty(sftpConfig.getUsername())){
					logger.error("创建SFTP Channel异常,未配置username");
					return sshSession;
				}
				if(StringUtils.isEmpty(sftpConfig.getPassword())){
					logger.error("创建SFTP Channel异常,未配置password");
					return sshSession;
				}
				if(StringUtils.isEmpty(sftpConfig.getHost())){
					logger.error("创建SFTP Channel异常,未配置host");
					return sshSession;
				}
				if(null==sftpConfig.getPort()){
					logger.error("创建SFTP Channel异常,未配置port");
					return sshSession;
				}
				JSch jsch = new JSch();
				sshSession = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), sftpConfig.getPort());
				logger.error("连接SFTP服务器成功[user="+sftpConfig.getUsername()+",host="+sftpConfig.getHost()+",port="+sftpConfig.getPort()+"]");
				sshSession.setPassword(sftpConfig.getPassword());
				Properties sshConfig = new Properties();
				sshConfig.put("StrictHostKeyChecking", "no");
				sshSession.setConfig(sshConfig);
				sshSession.connect();
				logger.error("登陆SFTP服务器成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				logger.error("创建SFTP Session出现异常");
				return null;
			}
		}
		return sshSession;
	}
	@Override
	public PooledObject<Session> makeObject() throws Exception {
		// TODO Auto-generated method stub
		return (PooledObject<Session>) new DefaultPooledObject<Session>(getConnectedSession());
	}

	@Override
	public void destroyObject(PooledObject<Session> p){
		// TODO Auto-generated method stub
		try {
			p.getObject().disconnect();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public boolean validateObject(PooledObject<Session> p) {
		// TODO Auto-generated method stub
		return p.getObject().isConnected();
	}

	@Override
	public void activateObject(PooledObject<Session> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(PooledObject<Session> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
