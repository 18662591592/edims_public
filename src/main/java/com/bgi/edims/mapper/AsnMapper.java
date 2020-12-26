package com.bgi.edims.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bgi.edims.model.EdiAsn;
import com.bgi.edims.model.EdiAsnRequest;

@Mapper
public interface AsnMapper {
	public int createEdiAsn(EdiAsn ediAsn);
	public int deleteEdiAsn(EdiAsn ediAsn);
	public int updateEdiAsn(EdiAsn ediAsn);
	public List<EdiAsn> getEdiAsns(EdiAsnRequest ediAsnRequest);
	public Integer getEdiAsnsCount(EdiAsnRequest ediAsnRequest);
	public List<EdiAsn> getEdiAsnsByAsnIds(String[] asnIds);
	public int deleteEdiAsns(String[] asnIds);
}
