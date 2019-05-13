package com.shrek.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.ShrekCategoryService;
import com.shrek.example.service.ShrekGoodsService;
import com.shrek.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("mobile")
public class MobileController {


    @Autowired
    private ShrekGoodsService shrekGoodsService;

    /**
     * 首页接口
     * @param request
     * @return
     */
    @PostMapping("index")
    public JSONObject index(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        JSONObject requestJson = new JSONObject();
        requestJson.put("swiper",shrekGoodsService.index(jsonObject));
        JSONObject section1Json = new JSONObject();
        jsonObject.put("sctionId",1);
        section1Json.put("list",shrekGoodsService.index(jsonObject));
        section1Json.put("banner","http://dummyimage.com/400x100/ffcc33/FFF.png&text=s1-banner");
        requestJson.put("section1",section1Json);

        JSONObject section2Json = new JSONObject();
        jsonObject.put("sctionId",2);
        section2Json.put("list",shrekGoodsService.sction2(jsonObject));
        section2Json.put("banner","http://dummyimage.com/400x100/ffcc33/FFF.png&text=s2-banner");
        requestJson.put("section2",section2Json);

        JSONObject section3Json = new JSONObject();
        jsonObject.put("sctionId",3);
        section3Json.put("list",shrekGoodsService.sction3(jsonObject));
        section3Json.put("banner","http://dummyimage.com/400x100/ffcc33/FFF.png&text=s3-banner");
        requestJson.put("section3",section3Json);

        return requestJson;
    }

    /**
     * 首页接口
     * @param request
     * @return
     */
    @GetMapping("index")
    public JSONObject index2(HttpServletRequest request) {
        return this.index(request);
    }

    /**
     * 商品详情接口
     * @param request
     * @return
     */
    @PostMapping("detail")
    public JSONObject detail(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        JSONObject requestJson = new JSONObject();
        requestJson.put("view",shrekGoodsService.detail(jsonObject));
        requestJson.put("swiper",shrekGoodsService.swiper(jsonObject));
        return requestJson;
    }

    /**
     * 分类商品详情接口
     * @param request
     * @return
     */
    @PostMapping("category")
    public JSONObject category(HttpServletRequest request) {
        JSONObject jsonObject=CommonUtil.request2Json(request);
        JSONObject requestJson = new JSONObject();
        requestJson.put("aside",shrekGoodsService.aside(jsonObject));
        return requestJson;
    }
}