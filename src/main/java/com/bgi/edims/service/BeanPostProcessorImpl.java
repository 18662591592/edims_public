package com.bgi.edims.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.bgi.edims.model.EdiMethod;

@Service
public class BeanPostProcessorImpl implements BeanPostProcessor {
    private Logger logger=LoggerFactory.getLogger(BeanPostProcessorImpl.class);
    private static Map<String, Object[]> bispMethodMap = null;
    public Object postProcessBeforeInitialization(Object bean, String beanName)
           throws BeansException {
       return bean;
    }
    public Object postProcessAfterInitialization(Object bean, String beanName)throws BeansException {
//	System.out.println("扫描bean="+beanName);
    Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
	for (Method method : methods) {
		EdiMethod ediMethod = method.getAnnotation(EdiMethod.class);
		if (ediMethod != null) {
		    Object[] methodInfos = new Object[3];
		    methodInfos[0] = bean;
		    methodInfos[1] = method;
		    methodInfos[2] = ediMethod.permission();
		    logger.info(beanName+"添加方法-"+ediMethod.value());
		    if(null==bispMethodMap){
		    	bispMethodMap=new HashMap<>();
		    }
		    bispMethodMap.put(ediMethod.value(), methodInfos);
		}
	    }
       return bean;
    }
	public Map<String, Object[]> getBispMethodMap() {
		return bispMethodMap;
	}
    
}