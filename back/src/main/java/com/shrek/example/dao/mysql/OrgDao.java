package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/** @version: 1.0 
* @Description: （对类进行功能描述） 
* @author: wushu吴署
* @date: 2018年9月17日
*/
public interface OrgDao {

	List<JSONObject> listOrg(JSONObject jsonObject);

	List<JSONObject> listOrgPk(JSONObject jsonObject);
	
	int updateOrg(JSONObject jsonObject);
	
	int insertOrg(JSONObject jsonObject);
	
	int deleteOrg(JSONObject jsonObject);
}
