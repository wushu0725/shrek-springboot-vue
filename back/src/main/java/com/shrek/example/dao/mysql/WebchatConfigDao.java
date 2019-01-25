package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * ${comments}
 * 
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2018-12-19 12:01:13
 */

public interface WebchatConfigDao {
    int update(JSONObject jsonObject);

    int insert(JSONObject jsonObject);

    List<JSONObject> listByPage(JSONObject jsonObject);

    List<JSONObject> list();

    List<JSONObject> listAll();


    int delete(Long id);

    int count(JSONObject jsonObject);

    JSONObject getByPkOrg(JSONObject jsonObject);
}
