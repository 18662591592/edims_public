package com.bgi.edims.sftp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bgi.edims.quartz.JobFactory;
@Configuration
public class SftpServiceConfig {
//	@Bean
//	public SftpChannelFactory sftpChannelFactory(SftpConfig sftpConfig) {
//		return new SftpChannelFactory(sftpConfig);
//	}
//
//	@Bean
//	public FTPClientFactory ftpClientFactory(FTPClientConfigure ftpClientConfigure) {
//		return new FTPClientFactory(ftpClientConfigure);
//	}
//	
//	
//	@Bean
//	public SftpChannelPool sftpChannelPool(SftpChannelFactory sftpChannelFactory) {
//		try {
//			return new SftpChannelPool(sftpChannelFactory);
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedOperationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	@Bean
//	public FTPClientPool ftpClientPool(FTPClientFactory ftpClientFactory) {
//		try {
//			return new FTPClientPool(ftpClientFactory);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
    @Bean
    public SchedulerFactoryBean schedulerFactory(JobFactory jobFactory){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        /*用于Quartz集群,启动时更新已存在的Job*/
        factoryBean.setOverwriteExistingJobs(true);
        /*定时任务开始启动后延迟5秒开始*/
        factoryBean.setStartupDelay(5);
        factoryBean.setJobFactory(jobFactory);
        return factoryBean;
    }
//    
//    @Bean
//    public CommonsMultipartResolver multipartResolver(){
//    	CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver();
//    	commonsMultipartResolver.setMaxUploadSize(104857600);
//    	commonsMultipartResolver.setMaxInMemorySize(4096);
//    	commonsMultipartResolver.setDefaultEncoding("UTF-8");
//    	return commonsMultipartResolver;
//    }
    
}
