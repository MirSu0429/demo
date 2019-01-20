package com.example.demo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.demo.dao.JobAndTriggerMapper;
import com.example.demo.entity.JobAndTrigger;
import com.example.demo.service.IJobAndTriggerService;
import com.example.demo.util.redis.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("jobAndTriggerImpl")
public class JobAndTriggerImpl extends ServiceImpl<JobAndTriggerMapper,JobAndTrigger> implements IJobAndTriggerService{

	@Resource
	private JobAndTriggerMapper jobAndTriggerMapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
		PageInfo<JobAndTrigger> page = null;
		PageHelper.startPage(pageNum, pageSize);
		/*List<Object> list1 = redisUtil.lGet("list1", 0, -1);
		List<JobAndTrigger> jobAndTriggers;
		if (list1 != null && list1.size() > 0) {
			jobAndTriggers = (List<JobAndTrigger>) list1.get(0);
			page = new PageInfo<JobAndTrigger>(jobAndTriggers);
		} else {
			List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
			redisUtil.lSet("list1",list);
			page = new PageInfo<JobAndTrigger>(list);
		}*/
		List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
		page = new PageInfo<JobAndTrigger>(list);
		return page;
	}

	@Override
	public List<JobAndTrigger> selectJobAndTriggerDetails() {
		return baseMapper.selectJobAndTriggerDetails();
	}

	@Override
	public JobAndTrigger getJobAndTriggerDetail(String jobClassName, String jobGroupName, String cronExpression) {
		return baseMapper. getJobAndTriggerDetail( jobClassName,  jobGroupName,  cronExpression);
	}

}