package com.bgi.edims.service;

import org.springframework.stereotype.Service;

import com.bgi.edims.model.EdiMethod;

@Service
public class EdiService {

	@EdiMethod(value="test")
	public String test() {
//		List<EdiSupplier> suppliers=userMapper.getEdiSuppliers(new EdiSupplier());
		return "service success";
	}
}
