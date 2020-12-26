package com.bgi.edims.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bgi.edims.model.EdiRole;
@Mapper
public interface RoleMapper {
	public int createEdiRole(EdiRole ediRole);
	public int deleteEdiRole(EdiRole ediRole);
	public int updateEdiRole(EdiRole ediRole);
	public List<EdiRole> getEdiRoles(EdiRole ediRole);
	public Integer getEdiRolesCount(EdiRole ediRole);
	public List<EdiRole> getEdiRolesByUserId(EdiRole ediRole);
	public int createEdiUserRoles(List<EdiRole> ediRoles);
}
