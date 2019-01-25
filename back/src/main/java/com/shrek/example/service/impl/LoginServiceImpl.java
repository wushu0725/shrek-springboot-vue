package com.shrek.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.dao.mysql.LoginDao;
import com.shrek.example.dao.mysql.UserDao;
import com.shrek.example.dao.mysql.WebChatCacheDao;
import com.shrek.example.service.LoginService;
import com.shrek.example.service.PermissionService;
import com.shrek.example.service.WebchatConfigService;
import com.shrek.example.util.CommonUtil;
import com.shrek.example.util.constants.Constants;
import com.shrek.example.util.constants.ErrorEnum;
import com.shrek.example.webchat.WebChatUtil;
import com.shrek.example.webchat.WebChatCommon;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: wus
 * @description: 登录service实现类
 */
@Service
public class LoginServiceImpl implements LoginService {
    private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private LoginDao loginDao;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private WebChatCacheDao webChatCacheDao;

	@Autowired
    private WebchatConfigService webchatConfigService;
    /**
     * 登录表单提交
     *
     * @param jsonObject
     * @return
     */
    @Override
	//@Action(name = "注解式拦截的add操作")
    public JSONObject authLogin(JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        JSONObject returnData = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            currentUser.login(token);
            returnData.put("result", "success");
        } catch (AuthenticationException e) {
            returnData.put("result", "fail");
        }
        return CommonUtil.successJson(returnData);
    }
    
    /**
     * 微信端登陆
     */
	@Override
	public JSONObject authLoginWeb(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		String code = jsonObject.getString("username");

		
		JSONObject queryJsonObject = new JSONObject();
		queryJsonObject.put("key", WebChatCommon.ACCESSTOKEN);
		String accessToken = this.getWebChatCache(queryJsonObject);
        JSONObject returnData = new JSONObject();
        //从微信获取用户名
        JSONObject webUserJsonObject = WebChatUtil.getUserByCode(accessToken, code);
        
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(webUserJsonObject.getString("UserId"), "111111");
        try {
            currentUser.login(token);
            returnData.put("result", "success"); 
        } catch (AuthenticationException e) {
            returnData.put("result", "fail");
        }
        return CommonUtil.successJson(returnData);
	}

    /**
     * 根据用户名和密码查询对应的用户
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
	//@Action(name = "注解式拦截的add操作")
    public JSONObject getUser(String username, String password) {
        return loginDao.getUser(username, password);
    }
    
    /**
     	* 微信端验证
     */
    @Override
    public JSONObject getUserWeb(String code) {
    	//根据code获取微信的userid	
    	JSONObject queryJsonObject = new JSONObject();
		queryJsonObject.put("key", WebChatCommon.ACCESSTOKEN);
		String accessToken = this.getWebChatCache(queryJsonObject);  	
		JSONObject webUserJsonObject= WebChatUtil.getUserByCode(accessToken, code);
    	String webUserName = webUserJsonObject.getString("UserId");
    	
    	queryJsonObject = new JSONObject();
    	queryJsonObject.put("username", webUserName);

    	JSONObject jsonObject = loginDao.getUserByWebUserName(queryJsonObject);
    	
    	//不存在，即第一次登陆，需要从微信端加载用户详细信息，并保存至数据
    	if(jsonObject==null) {
    		//查到用户的详细信息
    		
    		return CommonUtil.errorJson(ErrorEnum.E_500);
    	}else {
    		return jsonObject;
    	}
    	//return null;
    }
    
    /**
 	* 微信端通过工号验证
 */ 
	@Override
	public JSONObject getUserWeb2(String username) {
	//根据code获取微信的userid	
	JSONObject queryJsonObject = new JSONObject();
	
	queryJsonObject = new JSONObject();
	queryJsonObject.put("username", username);

	JSONObject jsonObject = loginDao.getUserByWebUserName(queryJsonObject);
	
	//不存在，即第一次登陆，需要从微信端加载用户详细信息，并保存至数据
	if(jsonObject==null) {
		//查到用户的详细信息
		
		return CommonUtil.errorJson(ErrorEnum.E_500);
	}else {
		return jsonObject;
	}
	//return null;
}

    /**
     * 根据返回的手机号码判断，有则更新信息，没有则新增
     * @param insertjsonObject
     */
    private void insertWebUserBefore(JSONObject insertjsonObject) {
		// TODO Auto-generated method stub
		String phone = insertjsonObject.getString("mobile");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", insertjsonObject.getString("userid"));
		
		if(insertjsonObject.getString("userid")!=null&&insertjsonObject.getString("userid").length()>0) {
			jsonObject.put("nickname", insertjsonObject.getString("name"));
			//默认角色停车员
			jsonObject.put("roleId", "3");
			jsonObject.put("webUsername", insertjsonObject.getString("userid"));
			//默认密码 111111
			jsonObject.put("password", "111111");
			if(phone!=null&&phone.length()>0) {
				JSONObject resultJson= loginDao.getUserByPhone(phone);
				if(resultJson!=null) { //有则根据电话号码把用户名，微信USERid，邮箱，姓名，性别更新掉
					userDao.updateUser(jsonObject);
				}else {
					userDao.addUser(jsonObject);
				}
			}else {
				userDao.addUser(jsonObject);
			}
		}
	}

	/**
     * 查询当前登录用户的权限等信息
     *
     * @return
     */
    @Override
    public JSONObject getInfo() {
        //从session获取用户信息
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        String username = userInfo.getString("username");
        JSONObject returnData = new JSONObject();
        JSONObject userPermission = permissionService.getUserPermission(username);
        session.setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
        returnData.put("userPermission", userPermission);
        return CommonUtil.successJson(returnData);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public JSONObject logout() {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        } catch (Exception e) {
        }
        return CommonUtil.successJson();
    }

    
	
	
	
	/**
	 * 获取微信token，先从本地数据库判断是否有未失效的，
	 * 有 则从本地读取出来，
	 * 没有 则从微信端获取来，在更新到数据库并缓存
	 * （最好是放缓存，此处还未加缓存）
	 * @return
	 */
    @Override
	public String getWebChatCache(JSONObject jsonObject1) {

		CommonUtil.fillOrgPk(jsonObject1);
		JSONObject jsonObject = webChatCacheDao.getWebChatCache(jsonObject1);

		if(jsonObject==null) {   //失效或者不存在，调用微信接口获取并保存数据库
			JSONObject configObject = webchatConfigService.getByPkOrg(jsonObject1);
			String accessToken = WebChatUtil.getAccessToken(configObject.getString("cortid"), configObject.getString("secret"));
			String txlAccessToken = WebChatUtil.getAccessToken(configObject.getString("cortid"), configObject.getString("txlsecret"));
			String jspApiTicket = WebChatUtil.getJSApiTicket(accessToken);
			webChatCacheDao.deleteWebChatCache(jsonObject1);//清掉之前的数据
			JSONObject insertObject = new JSONObject();
			CommonUtil.fillOrgPk(insertObject);
			//微信的token和JSapiticket默认失效2小时
			insertObject.put("expireTime", this.getBeforeByHourTime(2));
			insertObject.put("key", WebChatCommon.ACCESSTOKEN);
			insertObject.put("value", accessToken);
			webChatCacheDao.insertWebChatCache(insertObject);
			insertObject.put("key", WebChatCommon.JSAPITICKET);
			insertObject.put("value", jspApiTicket);
			webChatCacheDao.insertWebChatCache(insertObject);
			insertObject.put("key", WebChatCommon.TXLJSAPITICKET);
			insertObject.put("value", txlAccessToken);
			webChatCacheDao.insertWebChatCache(insertObject);
			if(WebChatCommon.ACCESSTOKEN.equals(jsonObject1.getString("key"))){
				return accessToken;
			}else if(WebChatCommon.JSAPITICKET.equals(jsonObject1.getString("key"))){
				return jspApiTicket;
			}else {
				return txlAccessToken;
			}
			
		}else {                    //有则直接从数据库返回
			return jsonObject.getString("value");
		}
	}
	
	/**
	 * 得到当前时间的后N小时
	 *
	 * @return
	 */
	public  Date getBeforeByHourTime(int ihour){ 
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + ihour);     
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");   
		//returnstr = df.format(calendar.getTime());    
		return calendar.getTime();  
	}  
}
