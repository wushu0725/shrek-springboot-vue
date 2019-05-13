package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.util.CommonUtil;
import java.util.List;
/**
 * ${comments}
 * 
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-07 14:26:31
 */

public interface ShrekGoodsDao {
    int update(JSONObject jsonObject);

    int insert(JSONObject jsonObject);

    List<JSONObject> listByPage(JSONObject jsonObject);

    int delete(Long id);

    int count(JSONObject jsonObject);

    List<JSONObject> index(JSONObject jsonObject);

    List<JSONObject> sction2(JSONObject jsonObject);

    List<JSONObject> sction3(JSONObject jsonObject);

    List<JSONObject> aside(JSONObject jsonObject);

    List<JSONObject> detail(JSONObject jsonObject);

    List<JSONObject> swiper(JSONObject jsonObject);

}
