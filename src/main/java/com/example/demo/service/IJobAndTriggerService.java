package com.example.demo.service;


import com.baomidou.mybatisplus.service.IService;
import com.example.demo.entity.JobAndTrigger;
import com.github.pagehelper.PageInfo;

public interface IJobAndTriggerService extends IService<JobAndTrigger> {
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
}
