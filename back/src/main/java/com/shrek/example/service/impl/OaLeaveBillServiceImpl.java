package com.shrek.example.service.impl;


import com.shrek.example.dao.mysql.OaLeaveBillDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.OaLeaveBillService;
import com.shrek.example.util.CommonUtil;
import java.util.List;

import com.shrek.example.util.constants.TaskStatusEnum;
import org.activiti.engine.RuntimeService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-05-23 14:48:36
 */
@Service
public class OaLeaveBillServiceImpl implements OaLeaveBillService {

    @Autowired
    private OaLeaveBillDao oaLeaveBillDao;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public JSONObject update(JSONObject jsonObject){
        oaLeaveBillDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        //待提交
        jsonObject.put("state",TaskStatusEnum.UNSUBMIT.getStatus());
        jsonObject.put("username", SecurityUtils.getSubject().getPrincipal().toString());
        oaLeaveBillDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        int count = oaLeaveBillDao.count(jsonObject);
        List<JSONObject> list = oaLeaveBillDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject delete (Long id){
        oaLeaveBillDao.delete(id);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject submit (Long id){
        JSONObject leaveBill = oaLeaveBillDao.selectById(id);
        //leaveBill.setState(TaskStatusEnum.CHECK.getStatus());
        leaveBill.put("state", TaskStatusEnum.CHECK.getStatus());


        String key = "LeaveBill";
        String businessKey = key + "_" + leaveBill.getString("id");
        runtimeService.startProcessInstanceByKey(key, businessKey);
        oaLeaveBillDao.update(leaveBill);

        return CommonUtil.successJson();
    }
}