package com.bgi.edims.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bgi.edims.mapper.MenuMapper;
import com.bgi.edims.mapper.RoleMapper;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMenu;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiRole;
import com.bgi.edims.model.EdiUser;

@Service
public class RoleService {
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private MenuMapper menuMapper;
	
	@Value("${edi.roleid}")
	private String roleId;
	private Logger logger=LoggerFactory.getLogger(RoleService.class);
	@EdiMethod(value="createEdiRole",permission="6create")
	@Transactional
	public void createEdiRole(EdiRole ediRole) {
		logger.info("[EdiMethod:createEdiRole]");
		String roleId=UUID.randomUUID().toString();
		ediRole.setRoleId(roleId);
		List<EdiMenu> ediMenus=menuMapper.getEdiMenus(new EdiMenu());
		roleMapper.createEdiRole(ediRole);
		for(EdiMenu ediMenu:ediMenus) {
			ediMenu.setRoleId(roleId);
		}
		menuMapper.createEdiRoleMenus(ediMenus);
	}
	@EdiMethod(value="deleteEdiRole",permission="6delete")
	@Transactional
	public void deleteEdiRole(EdiRole ediRole,EdiUser currentUser) throws EdiException {
		logger.info("[EdiMethod:deleteEdiRole]");
		if(null!=ediRole&&StringUtils.isNotEmpty(ediRole.getRoleId())) {
			if(roleId.equals(ediRole.getRoleId())) {
				throw new EdiException(EdiException.ERROR_CODE.INVALID, "管理员角色不可删除");
			}
			roleMapper.deleteEdiRole(ediRole);
			menuMapper.deleteEdiRoleMenus(ediRole);
		}
	}
	@EdiMethod(value="updateEdiRole",permission="6update")
	@Transactional
	public void updateEdiRole(EdiRole ediRole,EdiUser currentUser) throws EdiException {
		logger.info("[EdiMethod:updateEdiRole]");
		if(StringUtils.isEmpty(ediRole.getRoleId())) {
			createEdiRole(ediRole);
		}else {
			if(roleId.equals(ediRole.getRoleId())) {
				throw new EdiException(EdiException.ERROR_CODE.INVALID, "管理员角色不可删除");
			}
			roleMapper.updateEdiRole(ediRole);
		}
	}
	@EdiMethod(value="getEdiRoles")
	public EdiResponse<List<EdiRole>> getEdiRoles(EdiRole ediRole,EdiUser currentUser){
		logger.info("[EdiMethod:getEdiRoles]");
		EdiResponse<List<EdiRole>> ediResponse=new EdiResponse<>();
		try {
			Integer total=roleMapper.getEdiRolesCount(ediRole);
			ediResponse.setTotal(total);
			List<EdiRole> roles=roleMapper.getEdiRoles(ediRole);
			ediResponse.setRows(roles);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
			ediResponse.setMsg("获取角色列表出现异常");
		}
		return ediResponse;
	}
	@EdiMethod(value="getRoleMenus")
	public EdiResponse<List<EdiMenu>> getRoleMenus(EdiMenu ediMenu,EdiUser currentUser) {
		logger.info("[EdiMethod:getRoleMenus]");
		EdiResponse<List<EdiMenu>> ediResponse=new EdiResponse<>();
		try {
			List<EdiMenu> roleMenus=menuMapper.getEdiMenusByRoleId(ediMenu);
			ediResponse.setRows(roleMenus);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
			ediResponse.setMsg("获取角色菜单列表出现异常");
		}
		return ediResponse;
		
	}
	@EdiMethod(value="updateEdiRoleMenu")
	public void updateEdiRoleMenu(EdiMenu ediMenu,EdiUser currentUser) throws EdiException {
		logger.info("[EdiMethod:updateEdiRoleMenu]");
		if(null!=ediMenu&&StringUtils.isNotEmpty(ediMenu.getMenuId())&&StringUtils.isNotEmpty(ediMenu.getRoleId())) {
			if(roleId.equals(ediMenu.getRoleId())) {
				throw new EdiException(EdiException.ERROR_CODE.INVALID, "管理员角色不可编辑");
			}
			menuMapper.updateEdiRoleMenu(ediMenu);
		}
	}
}
