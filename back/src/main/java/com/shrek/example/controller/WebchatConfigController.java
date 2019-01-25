package com.shrek.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.WebchatConfigService;
import com.shrek.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("webchatConfig")
public class WebchatConfigController {

    @Autowired
    private WebchatConfigService webchatConfigService;

    @PostMapping
    public JSONObject insert(@RequestBody JSONObject requestJson) {
        return webchatConfigService.insert(requestJson);
    }

    @PutMapping
    public JSONObject update(@RequestBody JSONObject requestJson) {
        return webchatConfigService.update(requestJson);
    }

    @DeleteMapping
    public JSONObject delete(HttpServletRequest request) {
        return webchatConfigService.delete(Long.valueOf(request.getParameter("id")));
    }

    @GetMapping
    public JSONObject list(HttpServletRequest request) {
        JSONObject jsonObject= CommonUtil.request2Json(request);
        return webchatConfigService.list(jsonObject);
    }

    @GetMapping("/listAll")
    public JSONObject listAll(HttpServletRequest request) {
        JSONObject jsonObject= CommonUtil.request2Json(request);
        return webchatConfigService.listAll(jsonObject);
    }
}