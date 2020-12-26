package com.bgi.edims.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.bgi.edims.model.EdiMenu;
import com.bgi.edims.model.EdiRole;
import com.bgi.edims.model.EdiUser;
import com.bgi.edims.service.UserInnerService;
import com.bgi.edims.util.MD5;


public class EdiShiroRealm extends AuthorizingRealm implements  Realm,InitializingBean{

	@Autowired
	private UserInnerService userInnerService;
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    EdiUser user  = (EdiUser)principals.getPrimaryPrincipal();
	    EdiRole role=user.getRole();
	    if(null!=role) {
//	        authorizationInfo.addRole(role.getRoleName());
	    	List<String> roles=new ArrayList<>();
	    	roles.add(role.getRoleName());
	        for(EdiMenu p:role.getMenus()){
	        	if(p.getCreate()) {
	        		roles.add(p.getMenuId()+"create");
	        	}
	        	if(p.getDelete()) {
	        		roles.add(p.getMenuId()+"delete");
	        	}
	        	if(p.getUpdate()) {
	        		roles.add(p.getMenuId()+"update");
	        	}
	        	if(p.getQuery()) {
	        		roles.add(p.getMenuId()+"query");
	        	}
	        	if(p.getUpload()) {
	        		roles.add(p.getMenuId()+"upload");
	        	}
	        	if(p.getDownload()) {
	        		roles.add(p.getMenuId()+"download");
	        	}
	        }
	        authorizationInfo.addRoles(roles);
	    }
	    return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		    String username = (String)token.getPrincipal();
		    char[] passwords =  (char[])token.getCredentials();
		    String password=new String(passwords);
		    System.out.println(token.getCredentials());
		    if(StringUtils.isEmpty(username)){
		        return null;
		    }
		    EdiUser user = userInnerService.getEdiUserByUserName(username,MD5.getMD5(password));
		    if(user == null){
		        return null;
		    }
		    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
		    		user, //用户名
		            user.getMd5Pwd(), //密码
		            getName()  //realm name
		    );
		    return authenticationInfo;
	}

    
}