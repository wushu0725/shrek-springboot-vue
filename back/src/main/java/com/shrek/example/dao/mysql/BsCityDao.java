package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.util.CommonUtil;
import java.util.List;
/**
 * ${comments}
 * 
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-01-28 09:54:22
 */

public interface BsCityDao {
    int update(JSONObject jsonObject);

    int insert(JSONObject jsonObject);

    List<JSONObject> listByPage(JSONObject jsonObject);

    List<JSONObject> listAll(JSONObject jsonObject);

    List<JSONObject> listHot(JSONObject jsonObject);

    int delete(Long id);

    int count(JSONObject jsonObject);
}
