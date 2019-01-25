package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface WebChatUserDao {
	List<JSONObject> getWebChatUser(JSONObject jsonObject);
	
	int countWebChatUser(JSONObject jsonObject);
	
	int deleteWebChatUser(JSONObject jsonObject);
	
	int insertWebChatUser(JSONObject jsonObject);
}
