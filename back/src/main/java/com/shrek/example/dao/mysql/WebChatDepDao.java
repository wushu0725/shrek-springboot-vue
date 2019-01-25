package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface WebChatDepDao {
	List<JSONObject> getWebChatDep(JSONObject jsonObject);
	
	int deleteWebChatDep(JSONObject jsonObject);
	
	int insertWebChatDep(JSONObject jsonObject);
}
