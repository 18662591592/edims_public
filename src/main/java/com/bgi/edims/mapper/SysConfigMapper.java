package com.bgi.edims.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bgi.edims.model.EdiSysConfig;

@Mapper
public interface SysConfigMapper {
	public int createEdiSysConfig(EdiSysConfig ediSysConfig);
	public int deleteEdiSysConfig(EdiSysConfig ediSysConfig);
	public int updateEdiSysConfig(EdiSysConfig ediSysConfig);
	public List<EdiSysConfig> getEdiSysConfigs(EdiSysConfig ediSysConfig);
}
