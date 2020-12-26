package com.bgi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bgi.edims.quartz.QuartzManagerService;
@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
@ImportResource(locations={"classpath:applicationContext.xml"})
@ComponentScan(basePackages= {"com.bgi.edims"})
public class EdimsApplication extends SpringBootServletInitializer implements CommandLineRunner{
	@Autowired
	private QuartzManagerService quartzManagerService;
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(EdimsApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(EdimsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		quartzManagerService.startAsnScanJob();
		quartzManagerService.startInnerOrderScanJob();
		quartzManagerService.startOrderScanJob();
		quartzManagerService.startEmailScanJob();
	}
}
