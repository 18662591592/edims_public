package com.bgi.edims.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bgi.edims.model.EdiOrder;
import com.bgi.edims.model.EdiOrderRequst;

@Mapper
public interface OrderMapper {
	public int createEdiOrder(EdiOrder ediOrder);
	public int deleteEdiOrder(EdiOrder ediOrder);
	public int updateEdiOrder(EdiOrder ediOrder);
	public List<EdiOrder> getEdiOrders(EdiOrderRequst ediOrderRequst);
	public Integer getEdiOrdersCount(EdiOrderRequst ediOrderRequst);
	public List<EdiOrder> getEdiOrdersByOrderIds(String[] orderIds);
	public int deleteEdiOrders(String[] orderIds);
}
