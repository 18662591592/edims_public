package com.bgi.edims.sftp;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bgi.edims.model.EdiException;
import com.jcraft.jsch.Session;

public class SftpChannelPool implements ObjectPool<PooledObject<Session>> {
	private static final long SESSION_TIMEOUT = 600000L;//会话超时时间
	private Logger logger=LoggerFactory.getLogger(SftpChannelPool.class);
	private BlockingQueue<PooledObject<Session>> pool;
	private SftpChannelFactory sftpChannelFactory;
	private Integer poolSize=0;//线程池大小
	public SftpChannelPool(SftpChannelFactory sftpChannelFactory) throws IllegalStateException, UnsupportedOperationException, Exception{
		this.sftpChannelFactory=sftpChannelFactory;
		init();
	}
	public SftpChannelFactory getSftpChannelFactory(){
		return sftpChannelFactory;
	}
	/**
	 * 线程池初始化
	 * @throws IllegalStateException
	 * @throws UnsupportedOperationException
	 * @throws Exception
	 */
	private void init() throws IllegalStateException, UnsupportedOperationException, Exception {
		// TODO Auto-generated method stub
		if(sftpChannelFactory==null||null==sftpChannelFactory.getSftpConfig()||null==sftpChannelFactory.getSftpConfig().getMaxSize()||null==sftpChannelFactory.getSftpConfig().getMinSize()){
			 throw new EdiException(EdiException.ERROR_CODE.UNKNOWN, "sftpChannelFactory不存在或者SftpConfig未配置");
		}
		pool=new ArrayBlockingQueue<PooledObject<Session>>(sftpChannelFactory.getSftpConfig().getMaxSize()*2);
		for(int i=0;i<sftpChannelFactory.getSftpConfig().getMinSize();i++){
			addObject();
		}
	}
	
	
	private synchronized int poolSizeAdd(){
		return poolSize++;
	}
	private synchronized int poolSizeMinus(){
		return poolSize--;
	}
	

	@Override
	public PooledObject<Session> borrowObject(){
		// TODO Auto-generated method stub
	logger.error("请求SFTP连接，当前连接数："+poolSize+",空闲连接数："+pool.size());	
	PooledObject<Session> session=null;
	    try {
		if(pool.size()>0){
			logger.error("请求空闲SFTP连接");
			session=pool.take();
			//会话超时
			if(!session.getObject().isConnected()||(System.currentTimeMillis()-session.getLastUsedTime()>SESSION_TIMEOUT)){
				session=getReplacePooledObject(session);
			}
		}
		if(null==session){
			logger.error("SFTP连接为空，尝试创建新的连接，配置最大的连接数为："+sftpChannelFactory.getSftpConfig().getMaxSize());
			if(poolSize<sftpChannelFactory.getSftpConfig().getMaxSize()){
				session=sftpChannelFactory.makeObject();
				 pool.offer(session,3,TimeUnit.SECONDS);
				 poolSizeAdd();
			}else {
				logger.error("连接数已达到阈值，创建失败，配置最大的连接数为："+sftpChannelFactory.getSftpConfig().getMaxSize());
				return null;
			}
		}else if(!sftpChannelFactory.validateObject(session)){
			logger.error("请求空闲SFTP连接成功，但是连接不可用，尝试重新建立连接");
			invalidateObject(session);
			session=sftpChannelFactory.makeObject();
			pool.offer(session,3,TimeUnit.SECONDS);
			poolSizeAdd();
		}
	    } catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	    }
	    return session;
	}
	
	public void returnObject(PresapSftpChannel presapSftpChannel){
	    if(null!=presapSftpChannel.channel){
		presapSftpChannel.channel.disconnect();
	    }
	    returnObject(presapSftpChannel.session);
	}
	@Override
	public void returnObject(PooledObject<Session> session){
		// TODO Auto-generated method stub
		logger.error("即将返还SFTP连接，当前连接数："+poolSize+",空闲连接数："+pool.size());
		try {
		if(session==null){
			logger.error("连接返还失败~，返还连接为空~");
			poolSizeMinus();
		}else if (!pool.offer(session,3,TimeUnit.SECONDS)) {
		  	logger.error("连接返还失败~，即将销毁连接~");
		        sftpChannelFactory.destroyObject(session);
		        poolSizeMinus();
		} 
		} catch (Exception e) {
		    // TODO: handle exception
		    e.printStackTrace();
		}

		logger.error("返还SFTP连接完成，当前连接数："+poolSize+",空闲连接数："+pool.size());
	}

	@Override
	public void invalidateObject(PooledObject<Session> session) throws Exception {
		// TODO Auto-generated method stub
		pool.remove(session);
		poolSizeMinus();
		logger.error("销毁连接，当前连接数量："+poolSize+",空闲连接数："+pool.size());
	}

	/**
	 * 获取替代线程
	 * @param sessionPooledObject
	 * @return
	 * @throws Exception
	 */
	public PooledObject<Session>  getReplacePooledObject(PooledObject<Session> sessionPooledObject) throws Exception{
		logger.error("尝试建立替换连接~");
		pool.remove(sessionPooledObject);
		logger.error("原连接销毁成功~");
		PooledObject<Session>  replaceObject=getSftpChannelFactory().makeObject();	
		logger.error("替换连接新建成功~");
		return replaceObject;
	}
	@Override
	public void addObject() throws Exception, IllegalStateException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub
		 PooledObject<Session>  session=sftpChannelFactory.makeObject();
		if(null!=session){
		 pool.offer(session,3,TimeUnit.SECONDS);
		 poolSizeAdd();
		 logger.error("新增连接成功，当前连接数量："+poolSize+",空闲连接数："+pool.size());
		}else {
		logger.error("新增连接失败~，当前连接数量："+poolSize+",空闲连接数："+pool.size());
		}
	}

	@Override
	public int getNumIdle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumActive() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() throws Exception, UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	      while(pool.iterator().hasNext()){
			try {
				PooledObject<Session> session = pool.take();
				 sftpChannelFactory.destroyObject(session);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	 
	      }
	      poolSize=0;
	}
	
	public boolean uploadFile(String type, String pathName,String fileName,InputStream input){
		PooledObject<Session> pooledSession=borrowObject();
		try {
		    PresapSftpChannel presapSftpChannel=null;
			try {
			    presapSftpChannel=new PresapSftpChannel(pooledSession);
			} catch (Exception e) {
			    // TODO: handle exception
			    pooledSession=getReplacePooledObject(pooledSession);
			}
		    boolean result=presapSftpChannel.uploadFile(pathName, fileName, input);
		    presapSftpChannel.channel.disconnect();
		    return result;
		} catch (Exception e) {
		    // TODO: handle exception
		    return false;
		}finally{
			returnObject(pooledSession);
		}
	  }
	 public boolean uploadTextFile(String type, String pathName,String fileName,String context) throws UnsupportedEncodingException{
		ByteArrayInputStream is = new ByteArrayInputStream(context.getBytes("UTF-8"));
		return uploadFile(type, pathName, fileName, is);
	 }
}
