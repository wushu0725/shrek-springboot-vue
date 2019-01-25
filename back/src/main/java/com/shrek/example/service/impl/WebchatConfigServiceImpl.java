package com.shrek.example.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.shrek.example.dao.mysql.WebchatConfigDao;
import com.shrek.example.service.WebchatConfigService;
import com.shrek.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2018-12-19 12:01:13
 */
@Service
public class WebchatConfigServiceImpl implements WebchatConfigService {

    @Autowired
    private WebchatConfigDao webchatConfigDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        webchatConfigDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        webchatConfigDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        CommonUtil.fillOrgPk(jsonObject);
        int count = webchatConfigDao.count(jsonObject);
        List<JSONObject> list = webchatConfigDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject listAll(JSONObject jsonObject){
        List<JSONObject> list = webchatConfigDao.listAll();
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject delete (Long id){
        webchatConfigDao.delete(id);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getByPkOrg(JSONObject jsonObject) {
        return webchatConfigDao.getByPkOrg(jsonObject);
    }
}