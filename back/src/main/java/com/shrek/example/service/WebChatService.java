package com.shrek.example.service;

import com.alibaba.fastjson.JSONObject;

public interface WebChatService {
	JSONObject getSignInfo(JSONObject jsonObject);
	
	void sendMessageWebChat(JSONObject jsonObject);
}
