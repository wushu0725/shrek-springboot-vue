package com.shrek.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.OrgServie;
import com.shrek.example.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/** @version: 1.0 
* @Description: （对类进行功能描述） 
* @author: wushu吴署
* @date: 2018年9月17日
*/

@RestController
@RequestMapping("/org")
public class OrgController {
	
	@Autowired
	private OrgServie orgService;
	
	@GetMapping("/listOrg")
    public JSONObject listOrg(HttpServletRequest request) {
		return CommonUtil.successJson(orgService.listOrg(CommonUtil.request2Json(request)));
    }
	
	@RequiresPermissions("org:add")
	@PostMapping("/insertOrg")
    public JSONObject insertOrg(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "name,usable");
		return orgService.insertOrg(requestJson);
    }
	
	@RequiresPermissions("org:update")
	@PostMapping("/updateOrg")
    public JSONObject updateOrg(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "id");
		return orgService.updateOrg(requestJson);
    }
	
	@RequiresPermissions("org:delete")
	@GetMapping("/deleteOrg")
    public JSONObject deleteOrg(@RequestBody JSONObject requestJson) {
		CommonUtil.hasAllRequired(requestJson, "id");
		return orgService.deleteOrg(requestJson);
    }
}
