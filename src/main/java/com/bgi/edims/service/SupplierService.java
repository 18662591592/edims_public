package com.bgi.edims.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bgi.edims.mapper.SupplierMapper;
import com.bgi.edims.model.EdiException;
import com.bgi.edims.model.EdiMethod;
import com.bgi.edims.model.EdiResponse;
import com.bgi.edims.model.EdiSupplier;
import com.bgi.edims.model.EdiUser;

@Service
public class SupplierService {
	@Autowired
	private SupplierMapper supplierMapper;
	private Logger logger=LoggerFactory.getLogger(SupplierService.class);
	@EdiMethod(value="createEdiSupplier",permission="4create")
	public void createEdiSupplier(EdiSupplier ediSupplier,EdiUser currentUser) throws EdiException {
		try {
			supplierMapper.createEdiSupplier(ediSupplier);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "添加供应商出现异常");
		}
	}
	@EdiMethod(value="deleteEdiSupplier",permission="4delete")
	public void deleteEdiSupplier(EdiSupplier ediSupplier,EdiUser currentUser) {
		supplierMapper.deleteEdiSupplier(ediSupplier);
	}
	@EdiMethod(value="updateEdiSupplier",permission="4update")
	public void updateEdiSupplier(EdiSupplier ediSupplier) throws EdiException {
		if(null==ediSupplier) {
			throw new EdiException(EdiException.ERROR_CODE.INVALID, "修改的供应商不能为空");
		}else if (StringUtils.isEmpty(ediSupplier.getSupplierId())) {
			supplierMapper.createEdiSupplier(ediSupplier);
		}else {
			supplierMapper.updateEdiSupplier(ediSupplier);
		}
	}
	@EdiMethod(value="getEdiSuppliers")
	public EdiResponse<List<EdiSupplier>> getEdiSuppliers(EdiSupplier ediSupplier,EdiUser currentUser) {
		EdiResponse<List<EdiSupplier>> ediResponse=new EdiResponse<>();
		try {
			Integer total=supplierMapper.getEdiSuppliersCount(ediSupplier);
			ediResponse.setTotal(total);
			List<EdiSupplier> suppliers=supplierMapper.getEdiSuppliers(ediSupplier);
			ediResponse.setRows(suppliers);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ediResponse.setCode(EdiException.ERROR_CODE.ILLEGAL);
			ediResponse.setMsg("获取供应商列表出现异常");
		}
		return ediResponse;
	}
}
