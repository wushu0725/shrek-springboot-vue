package com.shrek.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.dao.mysql.OrgDao;
import com.shrek.example.service.OrgServie;
import com.shrek.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** @version: 1.0 
* @Description: （对类进行功能描述） 
* @author: wushu吴署
* @date: 2018年9月17日
*/
@Service
public class OrgServiceImpl implements OrgServie {
	
	@Autowired
	private OrgDao orgDao;

	@Override
	public List<JSONObject> listOrg(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		CommonUtil.fillOrgPk(jsonObject);

		List<JSONObject>  list = orgDao.listOrg(jsonObject);
		List<JSONObject>  listtemp = this.orgList(list);
		return listtemp;
	}

	@Override
	public List<JSONObject> listOrgPk(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		List<JSONObject>  list = orgDao.listOrgPk(jsonObject);
		return list;
	}
	
	public List<JSONObject> orgList(List<JSONObject> orgList){    
		List<JSONObject>  list = new ArrayList<JSONObject>();
		for (JSONObject obj : orgList) {   
			if("0".equals(obj.getString("parentId"))){
				obj.put("children", orgChild(obj.getString("id"),orgList));
				list.add(obj);
			}
		}	
		return list;
	}

	
	public List<JSONObject> orgChild(String id,List<JSONObject> orgList){
		List<JSONObject> lists = new ArrayList<JSONObject>();
		for(JSONObject a:orgList){
			if(a.getString("parentId").equals(id)){
				a.put("children", orgChild(a.getString("id"),orgList));	
				lists.add(a);
			}
		}
		return lists;
		
	}

	@Override
	public JSONObject updateOrg(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		orgDao.updateOrg(jsonObject);
		return CommonUtil.successJson();
	}

	@Override
	public JSONObject insertOrg(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		return CommonUtil.successJson(orgDao.insertOrg(jsonObject));
	}

	@Override
	public JSONObject deleteOrg(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		orgDao.deleteOrg(jsonObject);
		return CommonUtil.successJson();
	}
	
	
	

}
