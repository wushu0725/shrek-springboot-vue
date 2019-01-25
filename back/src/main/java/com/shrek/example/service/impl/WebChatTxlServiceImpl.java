package com.shrek.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.dao.mysql.LoginDao;
import com.shrek.example.dao.mysql.UserDao;
import com.shrek.example.dao.mysql.WebChatDepDao;
import com.shrek.example.dao.mysql.WebChatUserDao;
import com.shrek.example.service.LoginService;
import com.shrek.example.service.WebChatTxlService;
import com.shrek.example.util.CommonUtil;
import com.shrek.example.webchat.WebChatUtil;
import com.shrek.example.webchat.WebChatCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** @version: 1.0 
* @Description: （对类进行功能描述） 
* @author: wushu吴署
* @date: 2018年9月17日
*/
@Service
public class WebChatTxlServiceImpl implements WebChatTxlService {

	
	 
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private WebChatDepDao webChatDepDao;
    
    @Autowired
    private WebChatUserDao webChatUserDao;
    
    @Autowired
    private LoginDao loginDao;
    
    @Autowired
    private UserDao userDao;
    
    @Override
	public List<JSONObject> listWebChatOrg() {
		// TODO Auto-generated method stub
		JSONObject queryJsonObject = new JSONObject();
		CommonUtil.fillOrgPk(queryJsonObject);
    	List<JSONObject>  list = webChatDepDao.getWebChatDep(queryJsonObject);
    	List<JSONObject>  listtemp = this.orgList(list);

		return listtemp;
	}
    
    @Override
	public JSONObject listWebChatUser(JSONObject jsonObject) {
    	CommonUtil.fillPageParam(jsonObject);
		CommonUtil.fillOrgPk(jsonObject);
    	int count = webChatUserDao.countWebChatUser(jsonObject);
    	List<JSONObject> list = webChatUserDao.getWebChatUser(jsonObject);
    	JSONObject pageList= CommonUtil.successPage(jsonObject, list, count);
    	return pageList;
	}
    
    
    public List<JSONObject> orgList(List<JSONObject> orgList){    
		List<JSONObject>  list = new ArrayList<JSONObject>();
		for (JSONObject obj : orgList) {   
			if("0".equals(obj.getString("parentid"))){
				obj.put("children", orgChild(obj.getString("id"),orgList));
				list.add(obj);
			}
		}	
		return list;
	}

	
	public List<JSONObject> orgChild(String id,List<JSONObject> orgList){
		List<JSONObject> lists = new ArrayList<JSONObject>();
		for(JSONObject a:orgList){
			if(a.getString("parentid").equals(id)){
				a.put("children", orgChild(a.getString("id"),orgList));	
				lists.add(a);
			}
		}
		return lists;
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public JSONObject synchroTxl() {
		
		
		JSONObject queryJsonObject = new JSONObject();
		queryJsonObject.put("key", WebChatCommon.TXLJSAPITICKET);
		String accessToken = loginService.getWebChatCache(queryJsonObject);
		// TODO Auto-generated method stub
		CommonUtil.fillOrgPk(queryJsonObject);
		//微信通讯录先清空，在增加
		webChatDepDao.deleteWebChatDep(queryJsonObject);
		webChatUserDao.deleteWebChatUser(queryJsonObject);
		
		this.syschDep(accessToken);
		this.syschUser(accessToken);
		return CommonUtil.successJson();
	}
	
	
	private void syschDep(String accessToken) {
		JSONArray orgList= WebChatUtil.getAllDep(accessToken).getJSONArray("department");
		
		JSONObject orgJson = null;
		JSONObject orgJsontemp = null;
		
		
		for(int i= 0;i<orgList.size();i++) {
			orgJson = orgList.getJSONObject(i);
			orgJsontemp = new JSONObject();
			orgJsontemp.put("id", orgJson.getString("id"));
			orgJsontemp.put("name", orgJson.getString("name"));
			orgJsontemp.put("parentid", orgJson.getString("parentid"));
			CommonUtil.fillOrgPk(orgJsontemp);
			webChatDepDao.insertWebChatDep(orgJsontemp);
		}
		
	}
	
	private void syschUser(String accessToken) {
		
		
		JSONObject userJson = null;
		JSONObject userJsontemp = null;
		
		JSONArray userList= WebChatUtil.getAllUser(accessToken).getJSONArray("userlist");
		
		for(int i=0;i<userList.size();i++) {
			userJson = userList.getJSONObject(i);
			userJsontemp= new JSONObject();
			userJsontemp.put("name", userJson.getString("name"));
			userJsontemp.put("userid", userJson.getString("userid"));
			userJsontemp.put("mobile", userJson.getString("mobile"));
			userJsontemp.put("email", userJson.getString("email"));
			userJsontemp.put("avatar", userJson.getString("avatar"));
			userJsontemp.put("depId", userJson.getJSONArray("department").get(0));
			CommonUtil.fillOrgPk(userJsontemp);
			webChatUserDao.insertWebChatUser(userJsontemp);
			
		}
	}

	@Override
	public JSONObject addUser(JSONObject userObject) {
		// TODO Auto-generated method stub
		
		
		JSONObject jsonObject = loginDao.getUserByWebUserName(userObject);
		if(jsonObject == null) {//不存在，新增
			CommonUtil.fillOrgPk(userObject);
			userDao.addUser(userObject);
		}else {
			userDao.updateUserWebchat(userObject);
		}
		
		return CommonUtil.successJson();
	}

}
