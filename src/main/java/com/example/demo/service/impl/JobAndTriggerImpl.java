package com.example.demo.service.impl;

import java.util.List;

import com.example.demo.util.redis.RedisUtil;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.JobAndTriggerMapper;
import com.example.demo.entity.JobAndTrigger;
import com.example.demo.service.IJobAndTriggerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;


@Service
public class JobAndTriggerImpl implements IJobAndTriggerService{

	@Resource
	private JobAndTriggerMapper jobAndTriggerMapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		PageInfo<JobAndTrigger> page = null;
		List<Object> list1 = redisUtil.lGet("list1", 0, -1);
		List<JobAndTrigger> jobAndTriggers;
		if (list1 != null && list1.size() > 0) {
			jobAndTriggers = (List<JobAndTrigger>) list1.get(0);
			page = new PageInfo<JobAndTrigger>(jobAndTriggers);
		} else {
			List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
			redisUtil.lSet("list1",list);
			page = new PageInfo<JobAndTrigger>(list);
		}
		return page;
	}

}