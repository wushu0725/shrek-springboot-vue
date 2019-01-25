package com.shrek.example.service;

import com.shrek.example.MyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=MyApplication.class)
public class BusStopAreaInfoServiceTest {


//	@Test
//	public void testInsertBusStopAreaInfo() {
//		//fail("Not yet implemented");
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("buscode", "2-14007");
//		jsonObject.put("buslicence", "粤A33337");
//		jsonObject.put("busStopAreaId", "1");
//		jsonObject.put("inUsername", "wus");
//		
//		busStopAreaInfoService.insertBusStopAreaInfo(jsonObject);
//	}
	
//	@Test
//	public void testUpdateBusStopAreaInfo() {
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("id", "2");
//		jsonObject.put("outUsername", "wus");
//		
//		busStopAreaInfoService.updateBusStopAreaInfo(jsonObject);
//	}


}
