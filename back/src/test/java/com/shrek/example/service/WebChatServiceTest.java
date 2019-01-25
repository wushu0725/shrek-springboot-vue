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
public class WebChatServiceTest {

	@Autowired
	private WebChatService webChatService;
	
	@Test
	public void test() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("touser", "chenyl");
		jsonObject.put("toparty", "");
		jsonObject.put("totag", "");
		jsonObject.put("msgtype", "text");
		jsonObject.put("agentid", "1000020");
		JSONObject content = new JSONObject();
		content.put("content", "测试发送微信推送<a href='baidu.com'>W3School</a>");
		jsonObject.put("text", content);
		jsonObject.put("safe", "0");
		
		webChatService.sendMessageWebChat(jsonObject);
	}

}
