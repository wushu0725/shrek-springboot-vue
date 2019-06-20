package com.shrek.example.service;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.model.CommentDto;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ActTaskService {

    /**
     * 获取用户代办列表
     *
     * @param name
     * @return
     */
    JSONObject getTaskByName(JSONObject jsonObject, String name);

    /**
     * 通过任务ID查询任务信息关联申请单信息
     *
     * @param id
     * @return
     */
    JSONObject getTaskById(String id);

    /**
     * 提交任务
     *
     * @param leaveBillDto
     * @return
     */
    JSONObject submitTask(JSONObject leaveBillDto);

    /**
     * 通过任务ID 查询批注信息
     *
     * @param taskId 任务ID
     * @return
     */
    List<CommentDto> getCommentByTaskId(String taskId);

    /**
     * 追踪图片节点
     *
     * @param id
     * @return
     */
    InputStream viewByTaskId(String id);
}
