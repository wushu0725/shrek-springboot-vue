package com.shrek.example.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author shrek
 * @date 2018/7/29
 */
public interface SysGeneratorService {
	/**
	 * 生成代码
	 *
	 * @param tableNames 表名称
	 * @return
	 */
	byte[] generatorCode(String tableNames);

	/**
	 * 查询所有表
	 *
	 * @return
	 */
	JSONObject  queryAllTable();
}
