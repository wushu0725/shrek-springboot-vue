package com.shrek.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface WebChatTxlService {
	List<JSONObject> listWebChatOrg();
	
	JSONObject listWebChatUser(JSONObject jSONObject);
	
	
	JSONObject synchroTxl();
	
	JSONObject addUser(JSONObject jSONObject);
}
