package com.shrek.example.service.impl;


import com.shrek.example.dao.mysql.ShrekCategoryDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.ShrekCategoryService;
import com.shrek.example.util.CommonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-07 14:49:02
 */
@Service
public class ShrekCategoryServiceImpl implements ShrekCategoryService {

    @Autowired
    private ShrekCategoryDao shrekCategoryDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        shrekCategoryDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        shrekCategoryDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        int count = shrekCategoryDao.count(jsonObject);
        List<JSONObject> list = shrekCategoryDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject delete (Long id){
        shrekCategoryDao.delete(id);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject selectList(JSONObject jsonObject) {
        return CommonUtil.successJson(shrekCategoryDao.selectList(jsonObject));
    }
}