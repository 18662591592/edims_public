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
public class InnerOrderScanQuartzJob implements Job {
    @Autowired
    private SyncService syncService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
	    	JobDetail jobDetail =jobExecutionContext.getJobDetail();
	    	JobDataMap data = jobDetail.getJobDataMap();
	    	String scanPath=(String) data.get("scanPath");
	    	syncService.syncInnerOrders(scanPath);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

    }
}
