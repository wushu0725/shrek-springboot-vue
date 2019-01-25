package com.shrek.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/** @version: 1.0 
* @Description: （对类进行功能描述） 
* @author: wushu吴署
* @date: 2018年9月17日
*/
public interface OrgServie {
	List<JSONObject> listOrg(JSONObject jsonObject);

	JSONObject updateOrg(JSONObject jsonObject);

	JSONObject insertOrg(JSONObject jsonObject);

	JSONObject deleteOrg(JSONObject jsonObject);

	List<JSONObject> listOrgPk(JSONObject jsonObject);
}
