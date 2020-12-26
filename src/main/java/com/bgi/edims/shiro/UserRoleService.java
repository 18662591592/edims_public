package com.bgi.edims.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.bgi.edims.model.EdiException;

@Service
public class UserRoleService {

	
	public void hasRole(String role) throws EdiException {
		Subject subject=SecurityUtils.getSubject();
		if(subject.hasRole(role)) {
			System.out.println("user has role "+role);
		}else {
			throw new EdiException(EdiException.ERROR_CODE.NOPRO,role+"无操作权限");
		}
	}
}
