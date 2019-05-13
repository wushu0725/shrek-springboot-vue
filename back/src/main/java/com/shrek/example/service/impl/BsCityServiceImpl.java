package com.shrek.example.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.shrek.example.dao.mysql.BsCityDao;
import com.alibaba.fastjson.JSONObject;
import com.shrek.example.service.BsCityService;
import com.shrek.example.util.CommonUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 *
 * @author Wushu
 * @email 156810150@qq.com
 * @date 2019-01-28 09:54:22
 */
@Service
public class BsCityServiceImpl implements BsCityService {

    @Autowired
    private BsCityDao bsCityDao;

    @Override
    public JSONObject update(JSONObject jsonObject){
        bsCityDao.update(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject insert(JSONObject jsonObject){
        bsCityDao.insert(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject list(JSONObject jsonObject){
        CommonUtil.fillPageParam(jsonObject);
        int count = bsCityDao.count(jsonObject);
        List<JSONObject> list = bsCityDao.listByPage(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Override
    public JSONObject listAll(JSONObject jsonObject){
        String type = jsonObject.getString("type");
        if("group".equals(type)){
            return this.getGroupCity();
        }else if("guess".equals(type)){
            return JSONObject.parseObject("{\"pinyin\":\"guangzhou\",\"is_map\":true,\"longitude\":113.264359,\"latitude\":23.12908,\"sort\":6,\"area_code\":\"020\",\"abbr\":\"GZ\",\"name\":\"广州\",\"id\":4}");
        }else if("hot".equals(type)){
            return CommonUtil.successJson(this.getHotCity());
        }
        return null;
    }


    private List getHotCity(){
        JSONObject jsonObject=new JSONObject();
        List<JSONObject> list = bsCityDao.listHot(jsonObject);

        return list;
    }

    private JSONObject getGroupCity(){
        JSONObject jsonObject=new JSONObject();
        List<JSONObject> list = bsCityDao.listAll(jsonObject);
        JSONObject jsonObject1=new JSONObject();
        JSONObject reJsonObject = new JSONObject();
        JSONArray jsonArray = null;
        for(int j = 1;j<=26;j++) {
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    jsonObject1 = list.get(i);
                    String firstChat = jsonObject1.getString("abbr").substring(0,1);
                    if(firstChat.startsWith(String.valueOf((char) (64 + j)))){
                        if(reJsonObject.getJSONArray(firstChat)==null){
                            jsonArray = new JSONArray();
                            jsonArray.add(jsonObject1);
                            reJsonObject.put(firstChat,jsonArray);
                        }else{
                            reJsonObject.getJSONArray(firstChat).add(jsonObject1);
                        }
                    }
                }
            }
        }
        return reJsonObject;
    }

    @Override
    public JSONObject delete (Long id){
        bsCityDao.delete(id);
        return CommonUtil.successJson();
    }
}