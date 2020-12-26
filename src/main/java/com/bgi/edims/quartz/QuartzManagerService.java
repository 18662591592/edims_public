package com.bgi.edims.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.SysConfigMapper;
import com.bgi.edims.model.EdiSysConfig;
@Service
public class QuartzManagerService {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    
    @Autowired
    private SysConfigMapper sysConfigMapper;

    private Logger logger=LoggerFactory.getLogger(QuartzManagerService.class);
    private static String JOB_GROUP_NAME = "EDIMS_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME = "EDIMS_TRIGGERGROUP_NAME";
    
    public Map<String, String> getSystemConfigMap(){
    	Map<String, String> systemConfigs=new HashMap<>();
		EdiSysConfig paramConfig=new EdiSysConfig();
//		paramConfig.setType("2");
		List<EdiSysConfig> ediSysConfigs=sysConfigMapper.getEdiSysConfigs(paramConfig);
		for(EdiSysConfig ediSysConfig:ediSysConfigs) {
			systemConfigs.put(ediSysConfig.getConfigKey(), ediSysConfig.getConfigValue());
		}
		return systemConfigs;
    }
    
    public String getCronByInterval(String interval) {
//    	if(StringUtils.isEmpty(interval)) {
//    		return "";
//    	}else if("1".equals(interval)) {
//    		return "*/2 * * * * ?";
//    	}else if("2".equals(interval)) {
//    		return "*/2 * * * * ?";
//    	}else if("3".equals(interval)) {
//    		return "*/3 * * * * ?";
//    	}else if("4".equals(interval)) {
//    		return "*/4 * * * * ?";
//    	}else if("6".equals(interval)) {
//    		return "*/6 * * * * ?";
//    	}else if("12".equals(interval)) {
//    		return "*/12 * * * * ?";
//    	}else if("24".equals(interval)) {
//    		return "*/24 * * * * ?";
//    	}else if("0".equals(interval)) {
//    		return "*/10 * * * * ?";
//    	}
    	 	
    	if(StringUtils.isEmpty(interval)) {
    		return "";
    	}else if("1".equals(interval)) {
    		return "0 0 * * * ?";
    	}else if("2".equals(interval)) {
    		return "0 0 */2 * * ?";
    	}else if("3".equals(interval)) {
    		return "0 0 */3 * * ?";
    	}else if("4".equals(interval)) {
    		return "0 0 */4 * * ?";
    	}else if("6".equals(interval)) {
    		return "0 0 */6 * * ?";
    	}else if("12".equals(interval)) {
    		return "0 0 */12 * * ?";
    	}else if("24".equals(interval)) {
    		return "0 0 0 * * ?";
    	}else if("0".equals(interval)) {
    		return "*/20 * * * * ?";
    	}
    	return "";
    }
    
    public void startOrderScanJob() {
    	System.out.println("启动销售订单扫描进程");
    	Map<String, String> systemConfigs=getSystemConfigMap();
    	String path=systemConfigs.get("order_scan_path");
    	String cron=getCronByInterval(systemConfigs.get("order_scan_interval"));
    	String jobName="orderScanJob";
    	startScanJob(jobName, path, cron, OrderScanQuartzJob.class);
    }
    
    
    public void startAsnScanJob() {
    	logger.info("[asn]1-启动ASN扫描进程");
    	Map<String, String> systemConfigs=getSystemConfigMap();
    	String path=systemConfigs.get("asn_scan_path");
    	String interval=systemConfigs.get("asn_scan_interval");
    	String cron=getCronByInterval(interval);
    	String jobName="asnScanJob";
    	logger.info("[asn]2-启动参数  asn_scan_path="+path+",asn_scan_interval="+interval+",cron="+cron+",jobName="+jobName);
    	startScanJob(jobName, path, cron, AsnScanQuartzJob.class);
    }
    
    public void startInnerOrderScanJob() {
    	logger.info("[InnerOrder]1-启动内示订单扫描进程");
    	Map<String, String> systemConfigs=getSystemConfigMap();
    	String path=systemConfigs.get("inner_order_scan_path");
    	String cron=getCronByInterval(systemConfigs.get("inner_order_scan_interval"));
    	String jobName="innerorderScanJob";
    	startScanJob(jobName, path, cron, InnerOrderScanQuartzJob.class);
    }
    
    public void startEmailScanJob() {
    	logger.info("[email]1-启动邮件扫描进程");
    	Map<String, String> systemConfigs=getSystemConfigMap();
    	String cron=getCronByInterval(systemConfigs.get("email_scan_interval"));
    	String jobName="emailScanJob";
    	startScanJob(jobName, "", cron, EmailScanJob.class);
    }
    
	public void startScanJob(String jobName,String path,String cron,@SuppressWarnings("rawtypes") Class clazz) {
    	try {
    		
        	logger.info("[startScanJob]3-启动参数  path="+path+",cron="+cron+",jobName="+jobName);
        	Scheduler scheduler = schedulerFactoryBean.getScheduler();
        	JobKey jobKey=JobKey.jobKey(jobName,JOB_GROUP_NAME);
        	TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);
        	CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        	
        	if(StringUtils.isEmpty(cron)) {
        		logger.info("cron表达式为空，定时器退出");
                if (trigger == null){
                	return;
                }else{
            		scheduler.deleteJob(jobKey);
            		return;
                }
        	}
        	boolean isJobExists=scheduler.checkExists(jobKey);
        	logger.info("[startScanJob]4-任务是否已经存在？"+isJobExists);
            if (trigger == null){
            	startJob(scheduler, triggerKey, jobKey, path, cron, clazz);
            }else{
            	removeJob(trigger, jobKey);
                startJob(scheduler, triggerKey, jobKey, path, cron, clazz);
            }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

    }
    /**
     * 开启任务
     * @param scheduler
     * @param triggerKey
     * @param jobKey
     * @param path
     * @param cron
     * @param clazz
     * @throws SchedulerException
     */
    public void startJob(Scheduler scheduler,TriggerKey triggerKey,JobKey jobKey,String path,String cron,Class clazz) throws SchedulerException {
    	System.out.println("任务不存在，新建任务");
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).build();
        jobDetail.getJobDataMap().put("scanPath",path);
        jobDetail.getJobDataMap().put("scanCron",cron);
        //将cron表达式进行转换
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        //创建触发器并将cron表达式对象给塞入
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        //在调度器中将触发器和任务进行组合
        scheduler.scheduleJob(jobDetail,trigger);
    }
    /**
     * 移除任务
     * @param trigger
     * @param jobKey
     */
    public  void removeJob(CronTrigger trigger,JobKey jobKey) {  
        try {  
            Scheduler sched = schedulerFactoryBean.getScheduler();  
            sched.pauseTrigger(trigger.getKey());// 停止触发器  
            sched.unscheduleJob(trigger.getKey());// 移除触发器  
            sched.deleteJob(jobKey);// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
}
