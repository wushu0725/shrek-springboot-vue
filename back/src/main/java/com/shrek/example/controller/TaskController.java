package com.shrek.example.controller;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.ActTaskService;
import com.shrek.example.util.CommonUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("task")
public class TaskController {


    @Autowired
    private ActTaskService actTaskService;

    @GetMapping("/todo")
    public JSONObject todo(HttpServletRequest request) {
        return actTaskService.getTaskByName(CommonUtil.request2Json(request), SecurityUtils.getSubject().getPrincipal().toString());
    }

    @GetMapping("/{id}")
    public JSONObject getTaskById(@PathVariable String id) {
        return CommonUtil.successJson(actTaskService.getTaskById(id));
    }

    @PostMapping
    public JSONObject submitTask(@RequestBody JSONObject jsonObject) {
        return actTaskService.submitTask(jsonObject);
    }

    @GetMapping("/comment/{id}")
    public JSONObject commitList(@PathVariable String id) {
        return CommonUtil.successJson(actTaskService.getCommentByTaskId(id));
    }

    @GetMapping("/view/{id}")
    public ResponseEntity viewCurrentImage(@PathVariable String id) {
        InputStream imageStream = actTaskService.viewByTaskId(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity(IoUtil.readBytes(imageStream), headers, HttpStatus.CREATED);
    }

}
