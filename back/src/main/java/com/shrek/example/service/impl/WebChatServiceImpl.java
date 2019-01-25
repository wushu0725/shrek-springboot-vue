package com.shrek.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.LoginService;
import com.shrek.example.service.WebChatService;
import com.shrek.example.service.WebchatConfigService;
import com.shrek.example.util.CommonUtil;
import com.shrek.example.webchat.WebChatUtil;
import com.shrek.example.webchat.WebChatCommon;
import com.shrek.example.webchat.WebChatSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取微信SDK签名类
 * @author 吴署
 *
 */

@Service
public class WebChatServiceImpl implements WebChatService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private WebchatConfigService webchatConfigService;
	
	@Override
	public JSONObject getSignInfo(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
		//获取有效的JSAPITICK
		JSONObject queryObject = new JSONObject();
		queryObject.put("key", WebChatCommon.JSAPITICKET);
		String jsapiticke = loginService.getWebChatCache(queryObject);

		CommonUtil.fillOrgPk(queryObject);

		JSONObject configObject = webchatConfigService.getByPkOrg(queryObject);
	
		return WebChatSign.sign(jsapiticke, jsonObject.getString("url"),configObject.getString("cortid"));
	}

	@Override
	public void sendMessageWebChat(JSONObject content) {
		// TODO Auto-generated method stub
		JSONObject queryObject = new JSONObject();
		queryObject.put("key", WebChatCommon.ACCESSTOKEN);
		String accesstoken = loginService.getWebChatCache(queryObject);
	
		WebChatUtil.webchatSendMessage(content, accesstoken);
	}

}
