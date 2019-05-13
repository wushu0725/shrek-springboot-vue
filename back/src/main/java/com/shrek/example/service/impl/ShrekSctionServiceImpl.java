package com.shrek.example.service.impl;


import com.shrek.example.dao.mysql.ShrekSctionDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.ShrekSctionService;
import com.shrek.example.util.CommonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-07 15:28:05
 */
@Service
public class ShrekSctionServiceImpl implements ShrekSctionService {

    @Autowired
    private ShrekSctionDao shrekSctionDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        shrekSctionDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        shrekSctionDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        int count = shrekSctionDao.count(jsonObject);
        List<JSONObject> list = shrekSctionDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject delete (Long id){
        shrekSctionDao.delete(id);
        return CommonUtil.successJson();
    }
    @Override
    public JSONObject selectList(JSONObject jsonObject) {
        return CommonUtil.successJson(shrekSctionDao.selectList(jsonObject));
    }
}