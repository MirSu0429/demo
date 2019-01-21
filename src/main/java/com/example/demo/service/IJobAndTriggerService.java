package com.example.demo.service;


import com.baomidou.mybatisplus.service.IService;
import com.example.demo.entity.JobAndTrigger;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IJobAndTriggerService extends IService<JobAndTrigger> {
	PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);

	List<JobAndTrigger> selectJobAndTriggerDetails();

	JobAndTrigger getJobAndTriggerDetail(String jobClassName, String jobGroupName, String cronExpression);

	void addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception;

	void jobResume(String jobClassName, String jobGroupName) throws  Exception;

	void jobPause(String jobClassName, String jobGroupName) throws Exception;

	void jobDelete(String jobClassName, String jobGroupName) throws Exception;

	void jobReschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception;

}
