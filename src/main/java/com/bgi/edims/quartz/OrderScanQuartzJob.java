package com.bgi.edims.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.bgi.edims.service.SyncService;
@DisallowConcurrentExecution
public class OrderScanQuartzJob implements Job{
    @Autowired
    private SyncService syncService;
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			JobDetail jobDetail =jobExecutionContext.getJobDetail();
	    	JobDataMap data = jobDetail.getJobDataMap();
	    	String scanPath=(String) data.get("scanPath");
//	    	String scanCron=(String) data.get("scanCron");
//	    	System.out.println(jobDetail.getKey().getName()+" run scanPath=="+scanPath+",scanCron=="+scanCron);
	    	syncService.syncOrders(scanPath, false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
