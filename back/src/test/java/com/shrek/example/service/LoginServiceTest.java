package com.shrek.example.service;

import com.shrek.example.MyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=MyApplication.class)
public class LoginServiceTest {
	
	@Autowired
	private LoginService loginService;
	
	@Test
	public void testAuthLoginWeb() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", "111111111111");
		
		jsonObject.put("state", "222222222");
		
		loginService.authLoginWeb(jsonObject);
	}
}
