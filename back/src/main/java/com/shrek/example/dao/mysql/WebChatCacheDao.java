package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;

/** @version: 1.0 
* @Description: （对类进行功能描述） 
* @author: wushu吴署
* @date: 2018年9月17日
*/
public interface WebChatCacheDao {
	/**
     * 新增车辆
     * @param jsonObject
     * @return
     */
	JSONObject getWebChatCache(JSONObject jsonObject);

    /**
     * 统计车辆
     *
     * @param jsonObject
     * @return
     */
    int deleteWebChatCache(JSONObject jsonObject);

    /**
     * 车辆列表
     *
     * @param jsonObject
     * @return
     */
    int insertWebChatCache(JSONObject jsonObject);
}
