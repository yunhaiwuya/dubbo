package com.dubbo.imp;

import com.dubbo.service.TestService;

public class TestServiceImp implements TestService {

	@Override
	public String test() {
		System.out.println("test success");
		return "provider02";
	}

}
