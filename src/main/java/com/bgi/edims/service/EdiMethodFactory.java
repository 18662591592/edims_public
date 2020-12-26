package com.bgi.edims.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.shiro.UserRoleService;

@Service
public class EdiMethodFactory{
	private Logger logger=LoggerFactory.getLogger(BeanPostProcessorImpl.class);
    @Autowired
    private BeanPostProcessorImpl beanPostProcessorImpl;
    @Autowired
    private UserRoleService userRoleService;
    /**
     * 获取参数键值对
     * @param request
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
	Map<String, Object> paramsMap = new HashMap<>();
	Enumeration enumeration = request.getParameterNames();
	while (enumeration.hasMoreElements()) {
	    String paramName = (String) enumeration.nextElement();
	    String paramValue = request.getParameter(paramName);
	    // 形成键值对应的map
	    paramsMap.put(paramName, paramValue);
	}
	return paramsMap;
    }
    
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;  
        Object obj = beanClass.newInstance();  
        BeanUtils.populate(obj, map);  
        return obj;  
    } 
    
    public static Object getObjectFromRequest(HttpServletRequest request,Class<?> beanClass) throws Exception {
    	return mapToObject(getParameterMap(request), beanClass);
    }
    
	private EdiUser getCurrentUser() {
		Subject subject=SecurityUtils.getSubject();
		if(null==subject) {
			return null;
		}
		EdiUser ediUser= (EdiUser) subject.getPrincipal();
		return ediUser;
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public EdiResponse executeMethod(HttpServletRequest request,HttpServletResponse response) throws EdiException {
	try {
		EdiUser currentUser=getCurrentUser();
		if(null==currentUser||StringUtils.isEmpty(currentUser.getUserName())) {
			throw new EdiException(EdiException.ERROR_CODE.LOGOUT, "用户未登录");
		}
	    Map<String, Object> params = getParameterMap(request);
	    String method =(String) params.get("method");
	    logger.info("[接口反射]调用方法名称为" + method);
	    if (null == beanPostProcessorImpl.getBispMethodMap()) {
		throw new EdiException(EdiException.ERROR_CODE.INVALID,"edi方法未初始化");
	    }
	    Object[] methodInfos = beanPostProcessorImpl.getBispMethodMap().get(method);
	    if (null == methodInfos) {
		throw new EdiException(EdiException.ERROR_CODE.INVALID,"未找到方法[" + method + "]");
	    }

	    Object executeObject = methodInfos[0];
	    Method executeMethod = (Method) methodInfos[1];
	    String permission =  (String) methodInfos[2];
	    if(StringUtils.isNotEmpty(permission)) {
	    	userRoleService.hasRole(permission);
	    }
//	    
	    
	    List<Object> invokeArgs = new ArrayList<Object>();
	    Class<?>[] parameterTypes = executeMethod.getParameterTypes();
	    // 遍历参数
	    for(int i=0;i<parameterTypes.length;i++) {
	    	Class<?> parameterType=parameterTypes[i];
			if (HttpServletRequest.class.equals(parameterType)) {
			    invokeArgs.add(request);
			} else if (HttpServletResponse.class.equals(parameterType)) {
			    invokeArgs.add(response);
			} else if (EdiUser.class.equals(parameterType)) {
				invokeArgs.add(currentUser);
			} else {
				try {
					invokeArgs.add(transMap2Bean(params, parameterType));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					invokeArgs.add(null);
				}
			}
	    }
	    Object responObject=executeMethod.invoke(executeObject,invokeArgs.toArray());
	    if(EdiResponse.class.isInstance(responObject)) {
	    	return (EdiResponse) responObject;
	    }
	    EdiResponse ediResponse=new EdiResponse();
	    ediResponse.setRows(responObject);
	    return ediResponse;
	} catch (EdiException e) {
		e.printStackTrace();
	    throw e;
	} catch (Exception e) {
	    // TODO: handle exception
            Throwable cause = e.getCause();
            if(cause instanceof EdiException){
        	throw (EdiException)cause;
            }
            e.printStackTrace();
	    throw new EdiException(EdiException.ERROR_CODE.NETERROR,"系统异常");
	}
    }
    
    
    public static Object transMap2Bean(Map<String, Object> map,  Class<?> beanClass) {
    	
        try {
        	Object obj = beanClass.newInstance();  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
            	try {
                    String key = property.getName();

                    if (map.containsKey(key)) {
                        Object value = map.get(key);
                        // 得到property对应的setter方法
                        Method setter = property.getWriteMethod();
                        Class<?>[] parameterTypes =setter.getParameterTypes();
                        if(Integer.class.equals(parameterTypes[0])) {
                        	if(null!=value&&StringUtils.isNotEmpty(value.toString())) {
                        		setter.invoke(obj,Integer.parseInt(value.toString()) );
                        	}
                        }else if(Boolean.class.equals(parameterTypes[0])){
                        	if(null!=value&&StringUtils.isNotEmpty(value.toString())) {
                        		if("true".equals(value.toString())||"1".equals(value.toString())) {
                        			setter.invoke(obj,true);	
                        		}else {
                        			setter.invoke(obj,false);
                        		}
                        		
                        	}
                        }else{
                        	setter.invoke(obj, value);
						}                        
                    }
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(property.getName()+"出现异常");
					continue;
				}

            }
            return obj;
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return null;

        

    }
}
