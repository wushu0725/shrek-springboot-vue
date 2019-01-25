package com.shrek.example.webchat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/***
 * 
 * @author 获取微信授权信息
 *
 * 
 * 
 */

public class WebChatUtil {

	/***
	 * 模拟get请求
	 * @param url
	 * @param charset
	 * @param timeout
	 * @return
	 */
	public static String sendGet(String url, String charset, int timeout)
	{
		String result = "";
		try
		{
			URL u = new URL(url);
			System.out.println("url="+url);
			try
			{
				URLConnection conn = u.openConnection();
				conn.connect();
				conn.setConnectTimeout(timeout);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
				String line = "";
				while ((line = in.readLine()) != null)
				{
					result = result + line;
				}
				in.close();
			} catch (IOException e) {
				return result;
			}
		}
		catch (MalformedURLException e)
		{
			return result;
		}
		return result;

	}
	
	/***
	 * 模拟post请求
	 * @param url
	 * @param charset
	 * @param timeout
	 * @return
	 */
	public static String sendPost(String url, String charset, int timeout,String jsonStr)
	{
		String result = "";
		try
		{
			URL u = new URL(url);
			try
			{
				HttpURLConnection connection = (HttpURLConnection) u.openConnection();
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            connection.setUseCaches(false);
	            connection.setInstanceFollowRedirects(true);
	            connection.setConnectTimeout(timeout);
	            connection.setRequestMethod("POST"); // 设置请求方式
	            // connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
	            connection.setRequestProperty("Content-Type", "application/json");
	            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
	            out.append(jsonStr);
	            out.flush();
	            out.close();
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
				String line = "";
				while ((line = in.readLine()) != null)
				{
					result = result + line;
				}
				in.close();
			} catch (IOException e) {
				return result;
			}
		}
		catch (MalformedURLException e)
		{
			return result;
		}
		return result;

	}

	/***
	 * 获取acess_token
	 * 来源www.vxzsk.com
	 * @return
	 */

	public static String getAccessToken(String appid,String appSecret) {

		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+appid+"&corpsecret="+appSecret;
		
		String backData = WebChatUtil.sendGet(url, "utf-8", 10000);

		String accessToken = JSON.parseObject(backData).getString("access_token") ;

		return accessToken;

	}

	/***
	 * 
	 * 获取jsapiTicket
	 * 
	 * 来源 www.vxzsk.com
	 * 
	 * @return
	 * 
	 */

	public static String getJSApiTicket(String acessToken) {


		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token="+acessToken;

		String backData = WebChatUtil.sendGet(urlStr, "utf-8", 10000);

		String ticket = JSON.parseObject(backData).getString("ticket");

		return ticket;

	}
	
	
	
	/***
	 * 
	 * 微信根据code获取成员信息 见微信开发文档
	 * 
	 */

	public static JSONObject getUserByCode(String acessToken,String code) {


		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+acessToken+"&code="+code;

		String backData = WebChatUtil.sendGet(urlStr, "utf-8", 10000);

		return  JSON.parseObject(backData);
	}
	
	/***
	 * 
	 * 微信根据user_ticket获取成员详细信息 见微信开发文档
	 * 
	 */

	public static JSONObject getUserDetailByUserTicket(String userDetailTicket,String accessToken) {


		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserdetail?access_token="+accessToken;
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_ticket", userDetailTicket);
		String backData = WebChatUtil.sendPost(urlStr, "utf-8", 10000,jsonObject.toJSONString());

		return  JSON.parseObject(backData);
	}
	
	
	/***
	 * 
	 * 微信根据user_ticket获取成员详细信息 见微信开发文档
	 * 
	 */

	public static JSONObject webchatSendMessage(JSONObject jsonObject,String accessToken) {



		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+accessToken;
		
		String backData = WebChatUtil.sendPost(urlStr, "utf-8", 10000,jsonObject.toJSONString());

		return  JSON.parseObject(backData);
	}
	
	/***
	 * 
	 * 微信根据所有用户 见微信开发文档
	 * 
	 */

	public static JSONObject getAllUser(String accessToken) {


		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+accessToken+"&department_id=1&fetch_child=1";
		
		String backData = WebChatUtil.sendGet(urlStr, "utf-8", 10000);

		return  JSON.parseObject(backData);
	}
	
	/***
	 * 
	 * 微信根据所有用户 见微信开发文档
	 * 
	 */

	public static JSONObject getAllDep(String accessToken) {

		
		String urlStr = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+accessToken+"&id=";
		
		String backData = WebChatUtil.sendGet(urlStr, "utf-8", 10000);

		return  JSON.parseObject(backData);
	}
	

	public static void main(String[] args) {
		
		String token="8VftkAanhDR8XLkcF_jXZ4ygHIHlFvh8dTGKc7SWRdy6FBUNYEBlO-PLCvjJlime4_ssu6LeF9ypLS176_xOiAYd4rOcj23yROniVNERGFOAsXqtEQHQLWleoQeXecjPndAnxbRWX60nsw4iqcqQ9-cehiJl2jSx2iG1ui-6mAKgCi3bL6ITQZwQtfKwC366Q93wVUW0GDYkYSsxfpnFEw";
		JSONObject msg=new JSONObject();
		msg.put("touser", "chenyl");
		msg.put("toparty", "");
		msg.put("totag", "");
		msg.put("msgtype", "text");
		msg.put("agentid", "1000020");
		JSONObject content=new JSONObject();
		content.put("content", "测试发送信息222222222222");
		msg.put("text", content);
		msg.put("safe", "0");
		JSONObject result= WebChatUtil.webchatSendMessage(msg, token);
		
	/* String toke=WebCharUtil.getAccessToken("wxd5c40a32d49f2f38", "NMRTwu4ts5EhjwOjPlcJDO3SsiPBc3rWtLahud-CqOg");
	 System.out.println("=="+toke);*/

	}

}