package com.shrek.example.schedule;

import java.util.List;

import com.shrek.example.service.*;
import com.shrek.example.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


@Component
public class ScheduleTask {
	
	

	
	@Autowired
	private WebChatService webChatService;


	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${prop.upload-folder}")
	private String folders;

	
	/**d
	 * 每天10點執行  移出停车
	 */
    //@Scheduled(cron = "0 0 10 * * ?")
	//@Scheduled(fixedRate = 5000)
    public void removeAllStopBus() {
        //busStopAreaInfoService.updateAllBusStopAreaInfo();
    }


	//@Scheduled(cron = "0 0 10 * * ?")
//	@Scheduled(fixedRate = 5000)
//	public void test() {
//    	String dates= getYestoday();
//    	System.out.println(dates);
//    	String fole = new String(folders);
//		fole=fole+dates;
//		System.out.println(fole);
//	}
    
    //每天定时3点抽取数据
    //@Scheduled(cron = "0 0 3 * * ?")
    public void getPaibanDate() {

    	//计划停车 实时的 保存时间为昨天
		//paiBanLineBusPianService.downloadPaibanLineBusInfo();

		//维修数据 取昨天的维修数据保存
    	//PaiBanProjectService.downloadPaiBanProject(DateUtil.getYestoday());

    	//排班数据 取最新的排班数据
    	//paiBanInfoService.downloadPaibanInfo(DateUtil.getToday());

    }

	//每天定时4点执行报表存储过程
	//@Scheduled(cron = "0 0 4 * * ?")
	public void reportSchedule() {

		//busStopReportService.busStopCall();

	}


    //微信每天早上5点推送排班数据
    //@Scheduled(cron="0 0 5 * * ? ")
    public void sendMsgToDriverBy5Clock() {
    	JSONObject tempJson=null;
    	List<JSONObject> listJson=null;
    	for (int i = 0; i < listJson.size(); i++) {
			tempJson=listJson.get(i).getJSONObject("paibandriver");
			if(tempJson.get("oldpsncode")==null||"".equals(tempJson.get("oldpsncode"))) continue;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("touser", tempJson.get("oldpsncode"));
			jsonObject.put("toparty", "");
			jsonObject.put("totag", "");
			jsonObject.put("msgtype", "text");
			jsonObject.put("agentid", "111111");
			JSONObject content = new JSONObject();
			String msg=listJson.get(i).getString("drivername")+":本次发车时间"+tempJson.getString("linestartime")+" "+tempJson.getString("linename")+"线路,车牌号为"+tempJson.getString("buslicensenum");
			content.put("content", msg);
			jsonObject.put("text", content);
			jsonObject.put("safe", "0");
			webChatService.sendMessageWebChat(jsonObject);
		}

    
    }
    
    
}
