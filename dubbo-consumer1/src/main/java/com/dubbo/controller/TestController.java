package com.dubbo.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dubbo.service.TestService;

@Controller
public class TestController {

	@Resource(name="testService")
	private TestService testService;
	
	@RequestMapping(value = { "/consumer/testMain" })
	@ResponseBody
	public String testDubbo() {
		return "consuemr01" + testService.test();
	}

}
