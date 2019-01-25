package com.shrek.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.WebChatTxlService;
import com.shrek.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/webchattxl")
public class WebChatTxlController {

	@Autowired
	private WebChatTxlService webChatTxlService;
	
	
	@GetMapping("/listOrg")
    public JSONObject listWebChatOrg(HttpServletRequest request) {
		return CommonUtil.successJson(webChatTxlService.listWebChatOrg());
	}
	
	@GetMapping("/listUser")
    public JSONObject listWebChatUser(HttpServletRequest request) {
		JSONObject pageList=webChatTxlService.listWebChatUser(CommonUtil.request2Json(request));
		return pageList;
	}
	
	@GetMapping("/sysnTxl")
    public JSONObject sysnTxl(HttpServletRequest request) {
		webChatTxlService.synchroTxl();
		return CommonUtil.successJson();
	}
	
	@PostMapping("/addUser")
    public JSONObject addUser(@RequestBody JSONObject requestJson) {
       
        return webChatTxlService.addUser(requestJson);
    }
}
