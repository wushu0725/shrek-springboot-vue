package com.shrek.example.dao.mysql;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.util.CommonUtil;
import java.util.List;
/**
 * ${comments}
 * 
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-08 16:47:05
 */

public interface ShrekGoodsImgDao {
    int update(JSONObject jsonObject);

    int insert(JSONObject jsonObject);

    List<JSONObject> listByPage(JSONObject jsonObject);

    int delete(Long id);

    int count(JSONObject jsonObject);
}
