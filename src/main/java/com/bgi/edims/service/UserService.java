package com.bgi.edims.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.UserMapper;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiUser;

@Service
public class UserService {
	private Logger logger=LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserMapper userMapper;
	@EdiMethod(value="createEdiUser",permission="5create")
	public void createEdiUser(HttpServletRequest request,EdiUser currentUser) throws EdiException {
		logger.info("[EdiMethod:]");
		try {
			EdiUser ediUser=(EdiUser) EdiMethodFactory.getObjectFromRequest(request, EdiUser.class);
			if(StringUtils.isEmpty(ediUser.getPassWord())) {
				ediUser.setPassWord("000000");
			}
			ediUser.setMd5Pwd(ediUser.getPassWord());
			ediUser.setLastModifyUser(currentUser.getUserName());
			ediUser.setLastModifyTime(new Date());
			userMapper.createEdiUser(ediUser);
		} catch (EdiException e) {
			throw e;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "添加用户出现异常");
		}
	}
	@EdiMethod(value="updateEdiUser",permission="5update")
	public void updateEdiUser(HttpServletRequest request,EdiUser currentUser) throws EdiException {
		logger.info("[EdiMethod:]");
		try {
			EdiUser ediUser=(EdiUser) EdiMethodFactory.getObjectFromRequest(request, EdiUser.class);

			if(null==ediUser) {
				throw new EdiException(EdiException.ERROR_CODE.INVALID, "修改的供应商不能为空");
			}else if (StringUtils.isEmpty(ediUser.getUserId())) {
				ediUser.setLastModifyUser(currentUser.getUserName());
				if(StringUtils.isEmpty(ediUser.getPassWord())) {
					ediUser.setPassWord("000000");
				}
				ediUser.setMd5Pwd(ediUser.getPassWord());
				ediUser.setLastModifyTime(new Date());
				userMapper.createEdiUser(ediUser);
			}else {
				ediUser.setLastModifyUser(currentUser.getUserName());
				ediUser.setLastModifyTime(new Date());
				userMapper.updateEdiUser(ediUser);
			}
		} catch (EdiException e) {
			throw e;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "修改用户出现异常");
		}

	}
	@EdiMethod(value="deleteEdiUser",permission="5delete")
	public void deleteEdiUser(HttpServletRequest request) throws EdiException {
		logger.info("[EdiMethod:]");
		try {
			String userId=request.getParameter("userId");
			if(StringUtils.isEmpty(userId)) {
				throw new EdiException(EdiException.ERROR_CODE.INVALID, "用户ID不能为空");
			}
			EdiUser ediUser=new EdiUser();
			ediUser.setUserId(userId);
			userMapper.deleteEdiUser(ediUser);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "删除用户出现异常");
		}
		
	}
	@EdiMethod(value="getEdiUsers")
	public EdiResponse<List<EdiUser>> getEdiUsers(HttpServletRequest request,EdiUser currentUser) throws EdiException{
		logger.info("[EdiMethod:]");
		EdiResponse<List<EdiUser>> ediResponse=new EdiResponse<>();
		try {
			EdiUser ediUser=(EdiUser) EdiMethodFactory.getObjectFromRequest(request, EdiUser.class);
			Integer total=userMapper.getBaseEdiUsersCount(ediUser);
			ediResponse.setTotal(total);
			List<EdiUser> users=userMapper.getEdiUsers(ediUser);
			ediResponse.setRows(users);
		} catch (EdiException e) {
			throw e;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "获取用户列表出现异常");
		}
		return ediResponse;
	}
	@EdiMethod(value="changePwd")
	public void changePwd(HttpServletRequest request,EdiUser currentUser) throws EdiException {
		String password=request.getParameter("password");
		String repassword=request.getParameter("repassword");
		if(StringUtils.isEmpty(password)) {
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "用户密码不能为空");
		}
		if(!password.equals(repassword)) {
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "两次输入密码不一致");
		}
		EdiUser paramUser=new EdiUser();
		paramUser.setUserId(currentUser.getUserId());
		paramUser.setMd5Pwd(password);
		userMapper.updateEdiUser(paramUser);
	}
}
