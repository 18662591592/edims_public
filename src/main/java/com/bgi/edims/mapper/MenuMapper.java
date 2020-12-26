package com.bgi.edims.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bgi.edims.model.EdiMenu;
import com.bgi.edims.model.EdiRole;
@Mapper
public interface MenuMapper {
	public int createEdiMenu(EdiMenu ediMenu);
	public int deleteEdiMenu(EdiMenu ediMenu);
	public int updateEdiMenu(EdiMenu ediMenu);
	public List<EdiMenu> getEdiMenus(EdiMenu ediMenu);
	public Integer getEdiMenusCount(EdiMenu ediMenu);
	public List<EdiMenu> getEdiMenusByRoleId(EdiMenu ediMenu);
	public int createEdiRoleMenus(List<EdiMenu> ediMenus);
	public int deleteEdiRoleMenus(EdiRole ediRole);
	public int updateEdiRoleMenu(EdiMenu ediMenu);
	
}
