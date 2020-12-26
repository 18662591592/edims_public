package com.bgi.edims.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bgi.edims.model.EdiSupplier;
@Mapper
public interface SupplierMapper {
	public int createEdiSupplier(EdiSupplier ediSupplier);
	public int deleteEdiSupplier(EdiSupplier ediSupplier);
	public int updateEdiSupplier(EdiSupplier ediSupplier);
	public List<EdiSupplier> getEdiSuppliers(EdiSupplier ediSupplier);
	public Integer getEdiSuppliersCount(EdiSupplier ediSupplier);
}
