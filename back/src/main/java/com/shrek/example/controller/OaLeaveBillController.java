package com.shrek.example.controller;

import com.shrek.example.service.OaLeaveBillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.alibaba.fastjson.JSONObject;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.shrek.example.util.CommonUtil;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("oaLeaveBill")
public class OaLeaveBillController {

    @Autowired
    private OaLeaveBillService oaLeaveBillService;

    @PostMapping
    public JSONObject insert(@RequestBody JSONObject requestJson) {
        return oaLeaveBillService.insert(requestJson);
    }

    @PutMapping
    public JSONObject update(@RequestBody JSONObject requestJson) {
        return oaLeaveBillService.update(requestJson);
    }

    @DeleteMapping
    public JSONObject delete(HttpServletRequest request) {
        return oaLeaveBillService.delete(Long.valueOf(request.getParameter("id")));
    }

    @GetMapping
    public JSONObject list(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        return oaLeaveBillService.list(jsonObject);
    }

    @GetMapping("submit")
    public JSONObject submit(HttpServletRequest request) {
        return oaLeaveBillService.submit(Long.valueOf(request.getParameter("id")));
    }
}