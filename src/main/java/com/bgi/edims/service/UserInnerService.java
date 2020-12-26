package com.bgi.edims.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.UserMapper;
import com.bgi.edims.model.EdiUser;

@Service
public class UserInnerService {
	private Logger logger=LoggerFactory.getLogger(UserInnerService.class);
	@Autowired
	private UserMapper userMapper;
	/**
	 * 根据用户名获取用户
	 * @param userName
	 * @return
	 */
	public EdiUser getEdiUserByUserName(String userName,String md5pwd) {
		if(StringUtils.isNotEmpty(userName)) {
			EdiUser paramUser=new EdiUser();
			paramUser.setUserName(userName);
			paramUser.setMd5Pwd(md5pwd);
			List<EdiUser> ediUsers=userMapper.getEdiUsers(paramUser);
			if(null!=ediUsers&&ediUsers.size()==1) {
				return ediUsers.get(0);
			}
		}
		return null;
	}
}
